package com.piggod.blogadmin;

import cn.hutool.core.collection.ListUtil;
import com.piggod.common.domain.po.ArticleTag;
import com.piggod.common.service.IArticleTagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BlogAdminApplicationTests {

    @Autowired
    private IArticleTagService articleTagService;

    @Test
    void contextLoads() {
    }

    @Test
    void test() {

        List<Long> tagIds = ListUtil.toList();

                List<ArticleTag> list = articleTagService.lambdaQuery()
                .eq(ArticleTag::getArticleId, 2L)
                .notIn(ArticleTag::getTagId, tagIds).list();
    }

}
