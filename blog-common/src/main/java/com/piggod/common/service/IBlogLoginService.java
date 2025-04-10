package com.piggod.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.po.User;
import com.piggod.common.domain.vo.ResponseResult;

public interface IBlogLoginService{
    ResponseResult login(User user);

    ResponseResult logout();
}
