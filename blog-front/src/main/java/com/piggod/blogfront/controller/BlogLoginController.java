package com.piggod.blogfront.controller;

import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.UserDTO;
import com.piggod.common.domain.po.User;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.service.IBlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private IBlogLoginService blogLoginService;

    @SystemLog(bussinessName = "登录")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserDTO userDTO) {
        return blogLoginService.login(userDTO);
    }

    @SystemLog(bussinessName = "退出登录")
    @PostMapping("logout")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }

}
