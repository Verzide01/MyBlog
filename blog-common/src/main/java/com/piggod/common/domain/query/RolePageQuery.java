package com.piggod.common.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "角色分页查询条件")
public class RolePageQuery extends PageQuery {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "状态")
    private String status;

}
