package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.dto.UserInfoDTO;
import com.piggod.common.domain.dto.UserRegisterDTO;
import com.piggod.common.domain.po.LoginUser;
import com.piggod.common.domain.po.User;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.UserInfoVO;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.UserMapper;
import com.piggod.common.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.utils.RedisCache;
import com.piggod.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.piggod.common.constants.SystemConstants.BLOG_LOGIN_KEY_PREFIX;
import static com.piggod.common.enums.AppHttpCodeEnum.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-25
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult getUserInfo() {
        // 1.获取当前登录用户信息
        Long userId = SecurityUtils.getUserId();

        if (userId == null) {
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }

        // 2.查询用户信息
        // 先查redis redis没有查数据库并更新
        LoginUser loginUser = redisCache.getCacheObject(BLOG_LOGIN_KEY_PREFIX + userId);
        if (loginUser != null) {
            UserInfoVO userInfoVO = BeanUtil.toBean(loginUser.getUser(), UserInfoVO.class);
            return ResponseResult.okResult(userInfoVO);
        }

        User user = getById(userId);

        if (user == null) {
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 3.封装返回
        UserInfoVO userInfoVO = BeanUtil.toBean(user, UserInfoVO.class);

        return ResponseResult.okResult(userInfoVO);
    }

    @Override
    public ResponseResult updateUserInfo(UserInfoDTO userInfo) {
        // 1.校验数据合法性
        if (ObjectUtil.isEmpty(userInfo) || userInfo.getId() == null ){
            throw new SystemException(AppHttpCodeEnum.NOT_CHANGE_INFO);
        }

        // 1.1.判断更改的信息是否和当前登录用户为同一个人
        Long userId = SecurityUtils.getUserId();
        if (!userInfo.getId().equals(userId)) {
            // 不为同一个人 抛出异常
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        // 1.2.判断传入的数据和数据库是否有更改，无则抛出异常提示
        UserInfoVO userInfoVO = (UserInfoVO) getUserInfo().getData();
        if (ObjectUtil.equals(BeanUtil.toBean(userInfoVO, UserInfoDTO.class), userInfo)) {
            // 两个数据完全相等，提示输入更改
            throw new SystemException(AppHttpCodeEnum.NOT_CHANGE_INFO);
        }

        // 2.更新数据库
        User user = BeanUtil.toBean(userInfo, User.class);
        boolean save = updateById(user);

        if (!save){
            throw new SystemException(SAVE_UNSUCCESS);
        }else {
            // 删除旧的redis用户信息
            redisCache.deleteObject(BLOG_LOGIN_KEY_PREFIX + userId);

            // todo 这里其实可以用rabbitMQ异步更新
            // 替换新信息
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(getById(userId));
            redisCache.setCacheObject(BLOG_LOGIN_KEY_PREFIX+ userId, loginUser);

        }

        // 3.返回成功信息

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    @Transactional
    public ResponseResult register(UserRegisterDTO registerInfo) {
        // 1.校验数据 + 判断是否存在
        if (ObjectUtil.isEmpty(registerInfo)) {
            throw new SystemException(REGISTER_NOT_NULL);
        }
        if (userIsExist(registerInfo)) {
            // 用户已存在
            throw new SystemException(USER_IS_EXIST);
        }

        // 2.生成密码
        // 生成加密密码
        String encodePassword = passwordEncoder.encode(registerInfo.getPassword());

        User user = BeanUtil.toBean(registerInfo, User.class);
        user.setPassword(encodePassword);

        // 3.保存信息到数据库
        boolean save = save(user);
        if (!save) {
            throw new SystemException(SAVE_UNSUCCESS);
        }else {
            log.info("注册请求 - 用户名: {}, 昵称: {}, 邮箱: {}",
                    registerInfo.getUserName(), registerInfo.getNickName(),registerInfo.getEmail());
        }
        return ResponseResult.okResult(SUCCESS);
    }


    /**
     * registerInfo对象里除密码外任意一个字段的信息匹配 都认为是已经注册
     * @param registerInfo 用户注册信息
     * @return
     */
    @Override
    public boolean userIsExist(UserRegisterDTO registerInfo) {

        // 1. 检查是否有至少一个字段非空
        if (StrUtil.isAllBlank(registerInfo.getUserName(), registerInfo.getNickName(), registerInfo.getEmail(), registerInfo.getPhonenumber())) {
            return false;
        }
        // 构建查询条件
        // 动态添加查询条件
        // 执行查询
        // 如果 count > 0，说明用户已存在
        Integer count = lambdaQuery()
                .and(wrapper -> {
                    if (registerInfo.getUserName() != null && !registerInfo.getUserName().isEmpty()) {
                        wrapper.eq(User::getUserName, registerInfo.getUserName());
                    }
                    if (registerInfo.getNickName() != null && !registerInfo.getNickName().isEmpty()) {
                        wrapper.or().eq(User::getNickName, registerInfo.getNickName());
                    }
                    if (registerInfo.getEmail() != null && !registerInfo.getEmail().isEmpty()) {
                        wrapper.or().eq(User::getEmail, registerInfo.getEmail());
                    }
                    if (registerInfo.getPhonenumber() != null && !registerInfo.getPhonenumber().isEmpty()) {
                        wrapper.or().eq(User::getPhonenumber, registerInfo.getPhonenumber());
                    }
                })
                .count();


        return count > 0;
    }
}
