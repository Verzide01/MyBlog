package com.piggod.common.service;

import com.piggod.common.domain.po.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
public interface IRoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

}
