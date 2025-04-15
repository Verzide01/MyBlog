package com.piggod.blogadmin.controller;

import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.AddArticleDTO;
import com.piggod.common.domain.dto.UpdateArticleDTO;
import com.piggod.common.domain.po.Article;
import com.piggod.common.domain.query.ArticlePageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    public IArticleService articleService;

    @SystemLog(bussinessName = "写博文")
    @PreAuthorize("@ps.hasPermission('content:article:writer')")//权限控制
    @PostMapping
    public ResponseResult addArticle(@RequestBody @Valid AddArticleDTO addArticleDTO) {
        return articleService.addArticle(addArticleDTO);
    }

    @SystemLog(bussinessName = "后台分页查询文章")
    @GetMapping("/list")
    public ResponseResult list(@Valid ArticlePageQuery articlePageQuery){
        return articleService.selectArticleListByPage(articlePageQuery);
    }

    @SystemLog(bussinessName = "后台点击修改时根据id查询文章")
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id){
        return articleService.getArticleAndTagInfo(id);
    }

    @SystemLog(bussinessName = "写博文-更新文章")
    @PutMapping
    public ResponseResult updateArticle(@RequestBody UpdateArticleDTO updateArticleDTO){
        return articleService.updateArticle(updateArticleDTO);
    }

    @SystemLog(bussinessName = "后台删除(可批量)文章")
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable @NotBlank(message = "标签最小值为1") Long... id){
        return articleService.deleteArticleById(id);
    }


}
