package com.piggod.blogadmin.controller;


import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.AddTagDTO;
import com.piggod.common.domain.dto.PageDTO;
import com.piggod.common.domain.dto.UpdateTagDto;
import com.piggod.common.domain.query.PageQuery;
import com.piggod.common.domain.query.TagPageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 标签 前端控制器
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private ITagService tagService;

    @SystemLog(bussinessName = "条件分页查询标签")
    @GetMapping("/list")
    public ResponseResult listByPage(@Valid TagPageQuery tagPageQuery) {
        return tagService.listByPage(tagPageQuery);
    }

    @SystemLog(bussinessName = "根据id查询标签")
    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @SystemLog(bussinessName = "添加标签")
    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDTO addTagDTO) {
        return tagService.addTag(addTagDTO);
    }

    @SystemLog(bussinessName = "删除标签")
    @DeleteMapping("/{id}")
    public ResponseResult deleteTagById(@PathVariable Long id) {
        return tagService.deleteTag(id);
    }

    @SystemLog(bussinessName = "修改标签")
    @PutMapping
    public ResponseResult updateTag(@RequestBody UpdateTagDto updateTagDto){
        return tagService.updateTag(updateTagDto);
    }




}
