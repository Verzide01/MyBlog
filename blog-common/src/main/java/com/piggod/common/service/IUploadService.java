package com.piggod.common.service;

import com.piggod.common.domain.vo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

    ResponseResult upload(MultipartFile file) throws Exception;
}
