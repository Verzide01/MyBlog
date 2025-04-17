package com.piggod.blogadmin.controller;


import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.AddCategoryDTO;
import com.piggod.common.domain.dto.UpdateCategoryDTO;
import com.piggod.common.domain.query.CategoryPageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
//    @SystemLog(bussinessName = "导出分类xlsx表格文件") // 不能添加会导致数据转化失败
    @GetMapping("/export")
    public void exportCategory(HttpServletResponse response) {
        categoryService.exportCategory(response);
    }


    @SystemLog(bussinessName = "后台查询分类列表的信息")
    @GetMapping("/list")
    public ResponseResult listCategoryByPage(@Valid CategoryPageQuery query) {
        return categoryService.listCategoryByPage(query);
    }

    @SystemLog(bussinessName = "后台新增分类")
    @PostMapping
    public ResponseResult addCategory(@RequestBody @Valid AddCategoryDTO addCategoryDTO) {
        return categoryService.addCategory(addCategoryDTO);
    }



    @SystemLog(bussinessName = "后台删除分类")
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable @NotBlank(message = "标签最小值为1") Long... id) {
        return categoryService.deleteCategoryById(id);
    }

    @SystemLog(bussinessName = "后台修改分类时查询分类信息")
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable @Min(value = 1L, message = "标签最小值为1") Long id) {
        return categoryService.getCategoryById(id);
    }


    @SystemLog(bussinessName = "修改分类信息")
    @PutMapping
    public ResponseResult updateCategory(@RequestBody @Valid UpdateCategoryDTO updateCategoryDTO){
        return categoryService.updateCategory(updateCategoryDTO);
    }



}
