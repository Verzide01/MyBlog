package com.piggod.common.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.dto.PageDTO;
import com.piggod.common.domain.po.Article;
import com.piggod.common.domain.po.Category;
import com.piggod.common.domain.query.ArticlePageQuery;
import com.piggod.common.domain.vo.ArticleVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.mapper.ArticleMapper;
import com.piggod.common.mapper.CategoryMapper;
import com.piggod.common.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.piggod.common.constants.SystemConstants.ARTICLE_CONTENT_FIELD;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-11
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getHotArticleList() {
        // 1.查询前十条热门文章（根据浏览量降序排序） 展示文章标题和浏览量
        // 1.1.草稿不查询 删除的不查询
        List<Article> articles = lambdaQuery()
                .select(Article.class, info -> !info.getProperty().equals(ARTICLE_CONTENT_FIELD))
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .last("limit 10")
                .orderByDesc(Article::getViewCount)
                .list();
        if (articles == null || articles.isEmpty()) {
            // 查询为空则返回
            return ResponseResult.okResult();
        }

        // 2.封装

        List<ArticleVO> voList = BeanUtil.copyToList(articles, ArticleVO.class);

        return ResponseResult.okResult(voList);
    }

    @Override
    public ResponseResult getArticleListByPage(ArticlePageQuery query) {
        // 1.分页查询
        Page<Article> page = lambdaQuery()
                .select(Article.class, info -> !info.getProperty().equals(ARTICLE_CONTENT_FIELD))
                // 当有分类参数时根据分类查询
                .eq(query.getCategoryId() != null && query.getCategoryId() > 0,
                        Article::getCategoryId, query.getCategoryId())
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                // 第一页才用根据置顶降序排序
                .orderByDesc(SystemConstants.PAGE_QUERY_FIRST.equals(query.getPageNum()), Article::getIsTop)
                .page(new Page<>(query.getPageNum(), query.getPageSize()));

        List<Article> articles = page.getRecords();
        if (CollUtil.isEmpty(articles)){
            // 返回空值
            return ResponseResult.okResult(PageDTO.empty(page));
        }

        // 2.根据分类id查询分类信息
        Set<Long> categroyIds = articles.stream().map(Article::getCategoryId).collect(Collectors.toSet());
        List<Category> categories = categoryMapper.selectBatchIds(categroyIds);
        Map<Long, String> categroyMap = categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        // 3.封装vo
        List<ArticleVO> voList = BeanUtil.copyToList(articles, ArticleVO.class);
        for (ArticleVO vo : voList) {
            vo.setCategoryName(categroyMap.get(vo.getCategoryId()));
        }

        return ResponseResult.okResult(PageDTO.of(page, voList));
    }

    @Override
    public ResponseResult getArticleInfo(Long id) {
        // 1.根据id查询文章
        Article article = getById(id);
        if (article == null) {
            return ResponseResult.okResult(null);
        }

        // 2.根据分类id查询分类名字
        Long categoryId = article.getCategoryId();
        if (categoryId == null) {
            ArticleVO vo = BeanUtil.toBean(article, ArticleVO.class);
            return ResponseResult.okResult(vo);
        }
        Category category = categoryMapper.selectById(categoryId);

        // 3.封装vo
        ArticleVO vo = BeanUtil.toBean(article, ArticleVO.class);
        vo.setCategoryName(category.getName());
        return ResponseResult.okResult(vo);


    }
}
