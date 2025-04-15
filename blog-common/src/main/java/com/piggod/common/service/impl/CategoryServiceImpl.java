package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.po.Article;
import com.piggod.common.domain.po.Category;
import com.piggod.common.domain.vo.CategoryVO;
import com.piggod.common.domain.vo.ExcelCategoryVo;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.mapper.CategoryMapper;
import com.piggod.common.service.IArticleService;
import com.piggod.common.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.piggod.common.constants.SystemConstants.ARTICLE_CONTENT_FIELD;

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
}
