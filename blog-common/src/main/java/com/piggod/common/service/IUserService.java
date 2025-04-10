package com.piggod.common.service;

import com.piggod.common.domain.dto.UserInfoDTO;
import com.piggod.common.domain.dto.UserRegisterDTO;
import com.piggod.common.domain.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.vo.ResponseResult;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-25
 */
public interface IUserService extends IService<User> {

    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(UserInfoDTO userInfo);

    ResponseResult register(UserRegisterDTO registerInfo);

    /**
     * 根据字段信息查询数据库比对 判断是否存在该用户
     * 任意一个字段的信息匹配 都认为是已经注册
     * @param registerInfo 用户注册信息
     * @return
     */
    boolean userIsExist(UserRegisterDTO registerInfo);
}
