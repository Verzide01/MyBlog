package com.piggod.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.dto.AddCategoryDTO;
import com.piggod.common.domain.dto.UpdateCategoryDTO;
import com.piggod.common.domain.po.Category;
import com.piggod.common.domain.query.CategoryPageQuery;
import com.piggod.common.domain.vo.ResponseResult;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-14
 */
public interface ICategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCateGory();

    void exportCategory(HttpServletResponse response);

    ResponseResult listCategoryByPage(CategoryPageQuery query);

    ResponseResult addCategory(AddCategoryDTO addCategoryDTO);

    ResponseResult deleteCategoryById(Long[] id);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(UpdateCategoryDTO updateCategoryDTO);
}
