package com.piggod.common.service;

import com.piggod.common.domain.dto.AddMenuDTO;
import com.piggod.common.domain.dto.SelectMenuDTO;
import com.piggod.common.domain.dto.UpdateMenuDTO;
import com.piggod.common.domain.po.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.vo.MenuVO;
import com.piggod.common.domain.vo.ResponseResult;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
public interface IMenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<MenuVO> selectMenusUserId(Long userId);

    ResponseResult selectMenuList(SelectMenuDTO selectMenuDTO);

    ResponseResult addMenu(AddMenuDTO addMenuDTO);

    ResponseResult getMenuById(Long menuId);

    ResponseResult updateMenu(UpdateMenuDTO updateMenuDTO);

    ResponseResult deleteMenuById(Long[] id);

    boolean hasChild(Long[] menuId);
}
