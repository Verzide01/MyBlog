package com.piggod.common.cronjob;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.piggod.common.domain.po.Article;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.service.IArticleService;
import com.piggod.common.utils.RedisCache;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.piggod.common.constants.SystemConstants.ARTICLE_VIEW_COUNT;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IArticleService articleService;

    @Scheduled(cron = "0 0 0/1 * * ?") // 定时任务每一小时去redis读取数据 更新到数据库中    0 0 0/1 * * ? 是一小时  0/55 * * * * ?每五秒
    public void updateViewCount() {
        // 1.从redis获取数据
        Map<String, Integer> viewMap = redisCache.getCacheMap(ARTICLE_VIEW_COUNT);

        Set<String> keys = viewMap.keySet();

        List<Article> articles = keys.stream()
                .map(key -> new Article(Long.valueOf(key), Long.valueOf(viewMap.get(key)))).collect(Collectors.toList());



        // 2.更新到数据库中
        boolean save = articleService.updateBatchById(articles);

        if (!save){
            throw new SystemException(AppHttpCodeEnum.SAVE_UNSUCCESS);
        }

    }
}
