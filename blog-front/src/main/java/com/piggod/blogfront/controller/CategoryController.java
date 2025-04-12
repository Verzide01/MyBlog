package com.piggod.blogfront.controller;


import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @SystemLog(bussinessName = "查询分类信息")
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList() {
        return categoryService.getCategoryList();
    }

}
