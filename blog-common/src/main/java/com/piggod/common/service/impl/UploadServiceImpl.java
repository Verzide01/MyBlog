package com.piggod.common.service.impl;

import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.service.IUploadService;
import com.piggod.common.utils.AliyunOssUtils;
import com.piggod.common.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class UploadServiceImpl implements IUploadService {

    @Autowired
    private AliyunOssUtils aliyunOssUtils;


    @Override
    public ResponseResult upload(MultipartFile file) throws Exception {
        // 1.数据校验
        if (file.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.FILE_IS_NULL);
        }
        
        String originalFilename = file.getOriginalFilename();

        if (Objects.isNull(originalFilename)) {
            throw new SystemException(AppHttpCodeEnum.FILE_IS_NULL);
        }

        //对原始文件名进行判断大小。只能上传png、jpeg或jpg文件
        if(!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg") && !originalFilename.endsWith(".jpeg")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        String url = aliyunOssUtils.upload(file);


        return ResponseResult.okResult(url);
    }
}
