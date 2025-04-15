package com.piggod.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.dto.AddArticleDTO;
import com.piggod.common.domain.po.Article;
import com.piggod.common.domain.query.ArticlePageQuery;
import com.piggod.common.domain.vo.ResponseResult;

import java.util.Map;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-11
 */
public interface IArticleService extends IService<Article> {

    ResponseResult getHotArticleList();

    ResponseResult getArticleListByPage(ArticlePageQuery query);

    ResponseResult getArticleInfo(Long id);

    ResponseResult updateViewCount(Long id);

    /**
     * 根据文章id查询浏览量
     * @return
     */
    Map<String, Integer> getArticleViewCountMap();

    ResponseResult addArticle(AddArticleDTO addArticleDTO);

}
