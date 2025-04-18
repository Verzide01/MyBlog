package com.piggod.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeSelectVo {

    private Collection<Long> checkedKeys;

    private List<MenuTreeVo> menus;
}