package com.piggod.common.service;

import com.piggod.common.domain.po.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.query.CommentPageQuery;
import com.piggod.common.domain.vo.ResponseResult;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-03
 */
public interface ICommentService extends IService<Comment> {

    ResponseResult getCommentList(CommentPageQuery query);

    ResponseResult addComment(Comment comment);
}
