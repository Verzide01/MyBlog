package com.piggod.blogfront.controller;


import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.po.Article;
import com.piggod.common.domain.query.ArticlePageQuery;
import com.piggod.common.domain.vo.ArticleVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-11
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    public IArticleService articleService;

    @SystemLog(bussinessName = "查询热门文章列表")
    @GetMapping("/hotArticleList")
    public ResponseResult getHotArticleList(){
        return articleService.getHotArticleList();
    }

    @SystemLog(bussinessName = "分页查询文章列表")
    @GetMapping("/articleList")
    public ResponseResult getArticleListByPage(@Valid ArticlePageQuery query){
        return articleService.getArticleListByPage(query);
    }

    @SystemLog(bussinessName = "查询文章信息")
    @GetMapping("/{id}")
    public ResponseResult getArticleInfo(@PathVariable @Min(value = 1, message = "ID必须大于0") Long id){
        return articleService.getArticleInfo(id);
    }

    @SystemLog(bussinessName = "添加文章浏览量")
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable @Min(value = 1, message = "ID必须大于0") Long id){
        return articleService.updateViewCount(id);
    }




}
