package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.dto.AddLinkDTO;
import com.piggod.common.domain.dto.PageDTO;
import com.piggod.common.domain.dto.UpdateLinkDTO;
import com.piggod.common.domain.po.ArticleTag;
import com.piggod.common.domain.po.Link;
import com.piggod.common.domain.po.Tag;
import com.piggod.common.domain.query.LinkPageQuery;
import com.piggod.common.domain.vo.LinkVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.TagVO;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.LinkMapper;
import com.piggod.common.service.ILinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.piggod.common.constants.SystemConstants.VALUE_IS_ZERO;
import static com.piggod.common.constants.SystemConstants.VALUE_MIN_NUM;
import static com.piggod.common.enums.AppHttpCodeEnum.*;
import static com.piggod.common.enums.AppHttpCodeEnum.SUCCESS;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-17
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

    @Override
    public ResponseResult getAllLink() {
        // 1.查询所有友链
        List<Link> list = lambdaQuery()
                .eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL)
                .list();
        if(list == null || list.isEmpty()){
            return ResponseResult.okResult();
        }

        // 2.封装数据返回
        List<LinkVO> voList = BeanUtil.copyToList(list, LinkVO.class);
        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult listLinkByPage(LinkPageQuery query) {
        if (ObjUtil.isEmpty(query)){
            throw new SystemException(SYSTEM_ERROR);
        }

        // 1.分页查询标签
        Page<Link> page = lambdaQuery()
                .like(StrUtil.isNotEmpty(query.getName()), Link::getName, query.getName())
                .eq(StrUtil.isNotEmpty(query.getName()), Link::getStatus, query.getStatus())
                .page(query.toMpPage());

        List<Link> links = page.getRecords();
        if (links.isEmpty()){
            return ResponseResult.okResult(PageDTO.empty(page));
        }

        List<LinkVO> voList = BeanUtil.copyToList(links, LinkVO.class);

        return ResponseResult.okResult(PageDTO.of(page, voList));
    }

    @Override
    public ResponseResult addLink(AddLinkDTO addLinkDTO) {
        if (ObjUtil.isEmpty(addLinkDTO)){
            throw new SystemException(PARAM_INVALID);
        }

        Link link = BeanUtil.toBean(addLinkDTO, Link.class);
        boolean save = save(link);

        if (!save){
            throw new SystemException(ADD_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        if (ObjectUtil.isNull(id) || VALUE_IS_ZERO.equals(id)){
            throw new SystemException(VALUE_LITTLE_MIN_NUM);
        }

        Link link = lambdaQuery()
                .eq(id != null, Link::getId, id)
                .one();
        LinkVO linkVO = BeanUtil.toBean(link, LinkVO.class);
        return ResponseResult.okResult(linkVO);
    }

    @Override
    public ResponseResult deleteLinkById(Long[] id) {
        for (Long tagId : id) {
            if (tagId < VALUE_MIN_NUM){
                throw new SystemException(AppHttpCodeEnum.VALUE_LITTLE_MIN_NUM);
            }
        }

        List<Long> ids = ListUtil.toList(id);
        boolean remove = removeByIds(ids);

        if (!remove){
            throw new SystemException(DELETE_UNSUCCESS);
        }


        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult updateLink(UpdateLinkDTO updateLinkDTO) {
        if (ObjUtil.isEmpty(updateLinkDTO)){
            throw new SystemException(PARAM_INVALID);
        }


        Link link = BeanUtil.toBean(updateLinkDTO, Link.class);
        boolean update = updateById(link);
        if (!update){
            throw new SystemException(UPDATE_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }
}
