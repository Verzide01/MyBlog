package com.piggod.blogadmin.controller;


import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 标签 前端控制器
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private ITagService tagService;

    @GetMapping("/list")
    public ResponseResult list() {
        return ResponseResult.okResult(tagService.list());
    }

}
