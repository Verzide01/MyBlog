package com.piggod.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.po.Article;
import com.piggod.common.domain.query.ArticlePageQuery;
import com.piggod.common.domain.vo.ArticleVO;
import com.piggod.common.domain.vo.ResponseResult;

import java.util.List;

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
}
