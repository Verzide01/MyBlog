package com.piggod.blogadmin.controller;

import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private IUploadService uploadService;

    // 前端传入的图片字段为img 我们通过注解将img转为file
    @SystemLog(bussinessName = "后台上传头像、图片")
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("img") MultipartFile file) throws Exception {
        return uploadService.upload(file);
    }
}
