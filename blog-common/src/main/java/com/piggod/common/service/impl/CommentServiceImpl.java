package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piggod.common.domain.dto.CommentDTO;
import com.piggod.common.domain.dto.PageDTO;
import com.piggod.common.domain.po.Comment;
import com.piggod.common.domain.po.User;
import com.piggod.common.domain.query.CommentPageQuery;
import com.piggod.common.domain.vo.CommentVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.CommentMapper;
import com.piggod.common.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.piggod.common.constants.SystemConstants.*;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-03
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private IUserService userService;

    /**
     * 根据文章id查询父子评论，思路：先查根评论再查子评论
     * @param query 筛选条件有articleId、type
     * @return
     */
    @Override
    public ResponseResult getCommentList(CommentPageQuery query) {
        // 1.根据文章id查询评论
        // 2.筛选根评论
        if (query == null || query.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        Page<Comment> page = lambdaQuery()
                .eq(Comment::getRootId, ARTICLE_COMMENT_ROOT_ID)
                .eq(query.getArticleId() != null && ARTICLE_COMMENT.equals(query.getType()) ,
                        Comment::getArticleId, query.getArticleId())
                .eq(Comment::getType, query.getType())
                .page(query.toMpPage());

        List<Comment> rootCommentList = page.getRecords();

        if (rootCommentList.isEmpty()){
            return ResponseResult.okResult(PageDTO.empty(page));
        }

        // 3.封装 根评论 数据
        // 3.1.1用map集合收集评论信息
        Map<Long, Comment> rootCommentMap = rootCommentList.stream().collect(Collectors.toMap(Comment::getId, Function.identity()));

        // 3.1.2用set集合收集评论的用户和回复的用户id
        Map<Long, User> rootUserMap = getUserMap(rootCommentList);

        if (MapUtil.isEmpty(rootUserMap)){
            return ResponseResult.okResult(PageDTO.empty(page));
        }

        List<CommentVO> rootCommentVoList = toCommentVoList(rootCommentList, rootUserMap);

        // 4.封装 子评论 信息
        // 4.1.获取根评论的ids集合
        Set<Long> rootIds = rootCommentMap.keySet();

        // 4.2.根据根评论id查询子评论   传入根id 查询所有子评论
        List<Comment> childrenCommentList = lambdaQuery()
                .in(!rootIds.isEmpty(), Comment::getRootId, rootIds)
                .orderByAsc(Comment::getCreateTime)
                .list();

//        List<Comment> childrenCommentList = childrenCommentPage.getRecords();

        if (childrenCommentList.isEmpty()){
            // 为空 说明没有子评论 直接返回所有评论都是根评论的数据
            return ResponseResult.okResult(PageDTO.of(page, rootCommentVoList));
        }

        Map<Long, User> childrenUserMap = getUserMap(childrenCommentList);

        if (MapUtil.isEmpty(childrenUserMap)){
            // 为空 说明没有子评论 直接返回所有评论都是根评论的数据
            return ResponseResult.okResult(PageDTO.of(page, rootCommentVoList));
        }

        List<CommentVO> childrenCommentVoList = toCommentVoList(childrenCommentList, childrenUserMap);

        Map<Long, List<CommentVO>> childrenCommentMap =
                childrenCommentVoList.stream().collect(Collectors.groupingBy(CommentVO::getRootId));

        // 5.封装子评论到根评论中
        for (CommentVO rootCommentVo : rootCommentVoList) {
            // 5.1获取根id
            Long rootId = rootCommentVo.getId();
            // 5.2在childrenCommentMap中根据id查找对应的children集合
            List<CommentVO> children = childrenCommentMap.get(rootId);
            // 5.3封装
            rootCommentVo.setChildren(children);
        }


        return ResponseResult.okResult(PageDTO.of(page, rootCommentVoList));

    }

    @Override
    public ResponseResult addComment(CommentDTO commentDTO) {
        // 1.判断评论内容是否为空


        // 2.保存数据库
        // !!!其他字段使用了mybatisplus自动填充 可去MyMetaObjectHandler查看
        Comment comment = BeanUtil.toBean(commentDTO, Comment.class);
        boolean save = save(comment);
        // 如果保存失败抛出异常信息
        if (!save){
            throw new SystemException(AppHttpCodeEnum.SAVE_UNSUCCESS);
        }

        return ResponseResult.okResult();
    }

    private static List<CommentVO> toCommentVoList(List<Comment> rootCommentList, Map<Long, User> rootUserMap) {
        List<CommentVO> rootCommentVoList = BeanUtil.copyToList(rootCommentList, CommentVO.class);
        for (CommentVO rootCommentVo : rootCommentVoList) {
            // 3.2.1根据createBy查找用户 然后获取用户昵称
            User createUser = rootUserMap.get(rootCommentVo.getCreateBy());

            rootCommentVo.setUsername(
                    (createUser != null && createUser.getNickName() != null) ?
                            createUser.getNickName() : USER_CANCELED
            );

            // 3.2.1.1 设置用户头像字段信息
            rootCommentVo.setAvatar(createUser != null ? createUser.getAvatar() : null);

            // 3.2.2根据toCommentUserId查找用户 然后获取用户昵称（需检查是否为根评论）
            if (rootCommentVo.getToCommentUserId() != null && !rootCommentVo.getToCommentUserId().equals(ARTICLE_COMMENT_ROOT_ID)){
                User toCommentUser = rootUserMap.get(rootCommentVo.getToCommentUserId());

                rootCommentVo.setToCommentUserName(
                        (toCommentUser != null && toCommentUser.getNickName() != null) ?
                                toCommentUser.getNickName() : USER_CANCELED
                );

            }


        }
        return rootCommentVoList;
    }

    /**
     *  根据评论集合查询并返回一个key为userId value为user对象的Map集合
     * @param commentList
     * @return
     */
    private Map<Long, User> getUserMap(List<Comment> commentList) {
        Set<Long> userIds = commentList.stream()
                .flatMap(comment -> Stream.of(
                        comment.getCreateBy(),          // 创建者ID
                        comment.getToCommentUserId()    // 被回复用户ID
                ))
                .filter(Objects::nonNull)             // 过滤空值（可选）
                .collect(Collectors.toSet());

        // 用map集合收集用户信息
        Map<Long, User> userMap = null;

        if (!userIds.isEmpty()) {
            List<User> users = userService.listByIds(userIds);
            userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        }
        return userMap;
    }


}
