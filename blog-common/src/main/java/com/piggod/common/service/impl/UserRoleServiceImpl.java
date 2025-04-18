package com.piggod.common.service.impl;

import com.piggod.common.domain.po.UserRole;
import com.piggod.common.mapper.UserRoleMapper;
import com.piggod.common.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-18
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
