package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.dto.*;
import com.piggod.common.domain.po.*;
import com.piggod.common.domain.query.UserPageQuery;
import com.piggod.common.domain.vo.*;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.UserMapper;
import com.piggod.common.service.IRoleService;
import com.piggod.common.service.IUserRoleService;
import com.piggod.common.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.utils.RedisCache;
import com.piggod.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.piggod.common.constants.SystemConstants.*;
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

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

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
        if (ObjectUtil.isEmpty(userInfo) || userInfo.getId() == null) {
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

        if (!save) {
            throw new SystemException(SAVE_UNSUCCESS);
        } else {
            // 删除旧的redis用户信息
            redisCache.deleteObject(BLOG_LOGIN_KEY_PREFIX + userId);

            // todo 这里其实可以用rabbitMQ异步更新
            // 替换新信息
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.setUser(getById(userId));
            redisCache.setCacheObject(BLOG_LOGIN_KEY_PREFIX + userId, loginUser);

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
        if (userIsExist(registerInfo))  {
            // 用户已存在
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
        } else {
            log.info("注册请求 - 用户名: {}, 昵称: {}, 邮箱: {}",
                    registerInfo.getUserName(), registerInfo.getNickName(), registerInfo.getEmail());
        }
        return ResponseResult.okResult(SUCCESS);
    }


    /**
     * registerInfo对象里除密码外任意一个字段的信息匹配 都认为是已经注册
     * 其实这里写在impl层太冗余   可以用aop写出一个注解判断比较好
     *
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
        List<User> list = lambdaQuery()
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
                .list();

        if (CollUtil.isEmpty(list)) {
            // 查询为空 说明没有冲突（没有存在和数据库字段一样的用户）
            return false;
        }

        // 不为空的情况，抛出为空的对应字段，让用户知道哪个数据重复了
        for (User user : list) {
            // 记录异常信息
            log.error("注册请求 - 用户名: {}, 昵称: {}, 邮箱: {}, 错误信息: {}",
                    registerInfo.getUserName(), registerInfo.getNickName(), registerInfo.getEmail(), USER_EXIST.getMsg());

            if (StrUtil.equals(user.getUserName(), registerInfo.getUserName())) {
                throw new SystemException(USERNAME_EXIST);
            }
            if (StrUtil.equals(user.getNickName(), registerInfo.getNickName())) {
                throw new SystemException(NICKNAME_EXIST);
            }
            if (StrUtil.equals(user.getEmail(), registerInfo.getEmail())) {
                throw new SystemException(EMAIL_EXIST);
            }
            if (StrUtil.equals(user.getPhonenumber(), registerInfo.getPhonenumber())) {
                throw new SystemException(PHONENUMBER_EXIST);
            }
        }


        return true;
    }

    @Override
    public ResponseResult listUserByPage(UserPageQuery query) {
        if (ObjUtil.isEmpty(query)){
            throw new SystemException(SYSTEM_ERROR);
        }

        // 1.分页查询权限
        Page<User> page = lambdaQuery()
                .like(StrUtil.isNotEmpty(query.getUserName()), User::getUserName, query.getUserName())
                .like(StrUtil.isNotEmpty(query.getPhonenumber()), User::getPhonenumber, query.getPhonenumber())
                .eq(StrUtil.isNotEmpty(query.getStatus()), User::getStatus, query.getStatus())
                .page(query.toMpPage());

        List<User> users = page.getRecords();
        if (users.isEmpty()){
            return ResponseResult.okResult(PageDTO.empty(page));
        }

        List<UserVO> voList = BeanUtil.copyToList(users, UserVO.class);

        return ResponseResult.okResult(PageDTO.of(page, voList));
    }

    @Override
    @Transactional
    public ResponseResult addUser(AddUserDTO addUserDTO) {
        if (ObjUtil.isEmpty(addUserDTO)){
            throw new SystemException(PARAM_INVALID);
        }

        // 生成加密密码
        String encodePassword = passwordEncoder.encode(addUserDTO.getPassword());
        User user = BeanUtil.toBean(addUserDTO, User.class);
        user.setPassword(encodePassword);


        boolean save = save(user);

        if (!save){
            throw new SystemException(ADD_UNSUCCESS);
        }

        // 保存成功则添加数据到关联表
        List<Long> roleIds = addUserDTO.getRoleIds();
        if (roleIds.isEmpty()){
            // 说明为空不需要保存中间表
            return ResponseResult.okResult(SUCCESS);
        }

        List<UserRole> userRoleList = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult getUserById(Long id) {
        if (ObjectUtil.isNull(id) || VALUE_IS_ZERO.equals(id)){
            throw new SystemException(VALUE_LITTLE_MIN_NUM);
        }

        // 1.查询用户信息
        User user = lambdaQuery()
                .eq(id != null, User::getId, id)
                .one();
        UserInfoVO userInfoVO = BeanUtil.toBean(user, UserInfoVO.class);

        if (user == null){
            throw new SystemException(VALUE_LITTLE_MIN_NUM);
        }
        // 2.查询全部roles信息
        List<RoleVO> voList = (List<RoleVO>) roleService.listAllRole().getData();
        List<UserRole> urList = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, user.getId())
                .list();
        Set<Long> roleIds = urList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        UserInfoAndRoleIdsVo userInfoAndRoleIdsVo = new UserInfoAndRoleIdsVo(user, voList, roleIds);
        return ResponseResult.okResult(userInfoAndRoleIdsVo);
    }

    @Override
    @Transactional//涉及两张表更新、修改操作
    public ResponseResult updateUser(UpdateUserDTO updateUserDTO) {
        if (ObjUtil.isEmpty(updateUserDTO)) {
            throw new SystemException(SYSTEM_ERROR);
        }

        User user = BeanUtil.toBean(updateUserDTO, User.class);
        boolean update = updateById(user);

        if (!update) {
            throw new SystemException(UPDATE_UNSUCCESS);
        }
        // 更新用户信息成功 删除关联标签
        List<Long> roleIds = updateUserDTO.getRoleIds();

        // 删除旧的用户关联（无论 roleIds 是否为空）
        LambdaQueryWrapper<UserRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(deleteWrapper);

        // 插入新的标签关联（如果 menuIds 非空）
        if (CollUtil.isNotEmpty(roleIds)) {
            List<UserRole> newUserRoles = roleIds.stream()
                    .map(roleId -> new UserRole(user.getId(), roleId))
                    .collect(Collectors.toList());
            userRoleService.saveBatch(newUserRoles);
        }

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult deleteUserById(Long[] id) {
        for (Long tagId : id) {
            if (tagId < VALUE_MIN_NUM){
                throw new SystemException(AppHttpCodeEnum.VALUE_LITTLE_MIN_NUM);
            }
        }

        List<Long> ids = ListUtil.toList(id);
        boolean remove = removeByIds(ids);
        if (!remove){
            throw new SystemException(DELETE_UNSUCCESS);
        }
        // 删除中间表
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserRole::getUserId, ids);

        userRoleService.remove(wrapper);

        return ResponseResult.okResult(SUCCESS);
    }
}
