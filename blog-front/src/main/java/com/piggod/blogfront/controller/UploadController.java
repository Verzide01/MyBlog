package com.piggod.blogfront.controller;

import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private IUploadService uploadService;

    // 前端传入的图片字段为img 我们通过注解将img转为file
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("img") MultipartFile file) throws Exception {
        return uploadService.upload(file);
    }
}
