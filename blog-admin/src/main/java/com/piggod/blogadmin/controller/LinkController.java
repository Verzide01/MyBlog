package com.piggod.blogadmin.controller;

import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.AddLinkDTO;
import com.piggod.common.domain.dto.UpdateLinkDTO;
import com.piggod.common.domain.dto.UpdateTagDTO;
import com.piggod.common.domain.query.CategoryPageQuery;
import com.piggod.common.domain.query.LinkPageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.ILinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private ILinkService linkService;

    @SystemLog(bussinessName = "后台条件查询友链")
    @GetMapping("/list")
    public ResponseResult listLinkByPage(@Valid LinkPageQuery query ){
        return linkService.listLinkByPage(query);
    }

    @SystemLog(bussinessName = "后台新增友链")
    @PostMapping
    public ResponseResult addLink(@RequestBody  @Valid AddLinkDTO addLinkDTO){
        return linkService.addLink(addLinkDTO);
    }

    @SystemLog(bussinessName = "后台修改友链时根据id查询友链")
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable @Min(value = 1L, message = "标签最小值为1") Long id){
        return linkService.getLinkById(id);
    }

    @SystemLog(bussinessName = "后台修改友链")
    @PutMapping
    public ResponseResult updateLink(@RequestBody @Valid UpdateLinkDTO updateLinkDTO){
        return linkService.updateLink(updateLinkDTO);
    }

    @SystemLog(bussinessName = "后台删除友链")
    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkById(@PathVariable @NotBlank(message = "标签最小值为1") Long... id){
        return linkService.deleteLinkById(id);
    }

}