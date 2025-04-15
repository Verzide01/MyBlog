package com.piggod.blogadmin.controller;

import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.AddArticleDTO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    public IArticleService articleService;

    @SystemLog(bussinessName = "写博文")
    @PostMapping
    public ResponseResult addArticle(@RequestBody @Valid AddArticleDTO addArticleDTO) {
        return articleService.addArticle(addArticleDTO);
    }
}
