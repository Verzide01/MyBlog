package com.piggod.blogadmin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.UserInfo;
import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.UserDTO;
import com.piggod.common.domain.po.LoginUser;
import com.piggod.common.domain.po.Role;
import com.piggod.common.domain.po.User;
import com.piggod.common.domain.vo.AdminUserInfoVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.RoutersVO;
import com.piggod.common.domain.vo.UserInfoVO;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.service.IAdminLoginService;
import com.piggod.common.service.IBlogLoginService;
import com.piggod.common.service.IMenuService;
import com.piggod.common.service.IRoleService;
import com.piggod.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
public class AdminLoginController {



    @Autowired
    private IAdminLoginService adminLoginService;

    @SystemLog(bussinessName = "登录")
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody UserDTO userDTO) {
        return adminLoginService.login(userDTO);
    }


    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVO> getInfo() {
        return adminLoginService.getAdminUserInfoVO();
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVO> getRouters(){
        return adminLoginService.getRouters();
    }






    @SystemLog(bussinessName = "退出登录")
    @PostMapping("logout")
    public ResponseResult logout() {
        return adminLoginService.logout();
    }

}
