package com.piggod.common.service;


import com.piggod.common.domain.dto.UserDTO;
import com.piggod.common.domain.vo.ResponseResult;

public interface IBlogLoginService{
    ResponseResult login(UserDTO userDTO);

    ResponseResult logout();
}
