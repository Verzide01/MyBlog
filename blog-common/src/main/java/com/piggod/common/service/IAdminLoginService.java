package com.piggod.common.service;


import com.piggod.common.domain.dto.UserDTO;
import com.piggod.common.domain.vo.AdminUserInfoVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.RoutersVO;

public interface IAdminLoginService {
    ResponseResult login(UserDTO userDTO);

    ResponseResult logout();

    ResponseResult<AdminUserInfoVO> getAdminUserInfoVO();

    ResponseResult<RoutersVO> getRouters();
}
