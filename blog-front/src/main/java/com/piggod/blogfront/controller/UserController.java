package com.piggod.blogfront.controller;


import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.UserInfoDTO;
import com.piggod.common.domain.dto.UserRegisterDTO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.UserInfoVO;
import com.piggod.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @SystemLog(bussinessName = "查询用户信息")
    @GetMapping("/userInfo")
    public ResponseResult getUserInfo() {
        return userService.getUserInfo();
    }

    @SystemLog(bussinessName = "更新用户信息")
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody @Valid UserInfoDTO userInfo) {

        return userService.updateUserInfo(userInfo);
    }

    @SystemLog(bussinessName = "注册新用户")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Valid UserRegisterDTO registerInfo) {
        return userService.register(registerInfo);
    }

}
