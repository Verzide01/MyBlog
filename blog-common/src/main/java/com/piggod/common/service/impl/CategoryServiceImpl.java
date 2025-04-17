package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.dto.AddCategoryDTO;
import com.piggod.common.domain.dto.PageDTO;
import com.piggod.common.domain.dto.UpdateCategoryDTO;
import com.piggod.common.domain.po.Article;
import com.piggod.common.domain.po.Category;
import com.piggod.common.domain.query.CategoryPageQuery;
import com.piggod.common.domain.vo.CategoryVO;
import com.piggod.common.domain.vo.ExcelCategoryVo;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.CategoryMapper;
import com.piggod.common.service.IArticleService;
import com.piggod.common.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.piggod.common.constants.SystemConstants.*;
import static com.piggod.common.enums.AppHttpCodeEnum.*;
import static com.piggod.common.enums.AppHttpCodeEnum.SUCCESS;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-14
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private IArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        // 1.查询正式发布有文章的分类
        List<Article> articles = articleService.lambdaQuery()
                .select(Article.class, info -> !info.getProperty().equals(ARTICLE_CONTENT_FIELD))
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .list();

        // 2.用set集合过滤数据里重复的分类ID
        Set<Long> categoryIds = articles.stream().map(Article::getCategoryId).collect(Collectors.toSet());

        // 3.根据ids查询分类列表数据
        List<Category> list = lambdaQuery()
                .eq(Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL)
                .in(Category::getId, categoryIds)
                .list();

        List<CategoryVO> voList = BeanUtil.copyToList(list, CategoryVO.class);

        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult listAllCateGory() {
        // 查询所有状态正常的分类
        List<Category> list = lambdaQuery()
                .eq(Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL)
                .list();

        List<CategoryVO> voList = BeanUtil.copyToList(list, CategoryVO.class);

        return ResponseResult.okResult(voList);
    }

    @Override
    public void exportCategory(HttpServletResponse response) {
        try {
            List<Category> categoryList = lambdaQuery().list();
            List<ExcelCategoryVo> excelVoList = BeanUtil.copyToList(categoryList, ExcelCategoryVo.class);
            // 1.设置请求响应体
            WebUtils.setDownLoadHeader(SystemConstants.CATEGORY_EXCEL_DIR_NAME, response);
            // 2.上传文件
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(excelVoList);
        } catch (Exception e) {
            // 3.失败时返回异常信息
            response.reset();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @Override
    public ResponseResult listCategoryByPage(CategoryPageQuery query) {
        if (ObjUtil.isEmpty(query)){
            throw new SystemException(SYSTEM_ERROR);
        }

        // 1.分页查询标签
        Page<Category> page = lambdaQuery()

                .page(query.toMpPage());

        List<Category> categories = page.getRecords();
        if (categories.isEmpty()){
            return ResponseResult.okResult(PageDTO.empty(page));
        }

        List<CategoryVO> voList = BeanUtil.copyToList(categories, CategoryVO.class);

        return ResponseResult.okResult(PageDTO.of(page, voList));
    }

    @Override
    public ResponseResult addCategory(AddCategoryDTO categoryDTO) {
        if (ObjUtil.isEmpty(categoryDTO)){
            throw new SystemException(PARAM_INVALID);
        }

        Category category = BeanUtil.toBean(categoryDTO, Category.class);
        boolean save = save(category);

        if (!save){
            throw new SystemException(ADD_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult deleteCategoryById(Long[] id) {
        for (Long categoryId : id) {
            if (categoryId < VALUE_MIN_NUM){
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
    public ResponseResult getCategoryById(Long id) {
        if (ObjectUtil.isNull(id) || VALUE_IS_ZERO.equals(id)){
            throw new SystemException(VALUE_LITTLE_MIN_NUM);
        }

        Category tag = lambdaQuery()
                .eq(id != null, Category::getId, id)
                .one();
        CategoryVO tagVO = BeanUtil.toBean(tag, CategoryVO.class);
        return ResponseResult.okResult(tagVO);
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryDTO updateCategoryDTO) {
        if (ObjUtil.isEmpty(updateCategoryDTO)){
            throw new SystemException(PARAM_INVALID);
        }


        Category category = BeanUtil.toBean(updateCategoryDTO, Category.class);
        boolean update = updateById(category);
        if (!update){
            throw new SystemException(UPDATE_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }
}
