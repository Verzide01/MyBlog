package com.piggod.common.service.impl;

import com.piggod.common.domain.po.ArticleTag;
import com.piggod.common.mapper.ArticleTagMapper;
import com.piggod.common.service.IArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签关联表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-15
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {

}
