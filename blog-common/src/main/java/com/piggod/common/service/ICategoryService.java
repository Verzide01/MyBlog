package com.piggod.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.po.Category;
import com.piggod.common.domain.vo.ResponseResult;

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
}
