package com.piggod.common.service.impl;

import com.piggod.common.domain.po.RoleMenu;
import com.piggod.common.mapper.RoleMenuMapper;
import com.piggod.common.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-17
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

}
