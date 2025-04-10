package com.piggod.blogfront.controller;


import com.piggod.common.domain.po.Comment;
import com.piggod.common.domain.query.CommentPageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.piggod.common.constants.SystemConstants.ARTICLE_COMMENT;
import static com.piggod.common.constants.SystemConstants.LINK_COMMENT;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-03
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult getCommentList(@Valid CommentPageQuery query) {
        // 使用了aop切面编程 将query.setType = ARTICLE_COMMENT
        // query.setType(ARTICLE_COMMENT);
        return commentService.getCommentList(query);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult getLinkCommentList(@Valid CommentPageQuery query) {
        // 使用了aop切面编程 将query.setType = LINK_COMMENT
        // query.setType(LINK_COMMENT);
        return commentService.getCommentList(query);
    }

}
