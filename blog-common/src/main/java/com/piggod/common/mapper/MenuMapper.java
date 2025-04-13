package com.piggod.common.mapper;

import com.piggod.common.domain.po.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(@Param("id") Long id);
}
