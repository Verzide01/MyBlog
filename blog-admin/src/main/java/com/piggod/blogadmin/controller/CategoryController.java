package com.piggod.blogadmin.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.piggod.common.annotation.SystemLog;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.po.Category;
import com.piggod.common.domain.vo.ExcelCategoryVo;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.service.ICategoryService;
import com.piggod.common.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-15
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {


    @Autowired
    private ICategoryService categoryService;

    @SystemLog(bussinessName = "查询所有状态正常的分类")
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        return categoryService.listAllCateGory();
    }


    @PreAuthorize("@ps.hasPermission('content:category:export')")//权限控制，ps是PermissionService类的bean名称
//    @SystemLog(bussinessName = "导出分类xlsx表格文件") 不能添加会导致数据转化失败
    @GetMapping("/export")
    public void exportCategory(HttpServletResponse response) {
        categoryService.exportCategory(response);
    }



}
