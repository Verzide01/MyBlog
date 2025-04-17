package com.piggod.common.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "友链分页查询条件")
public class LinkPageQuery extends PageQuery {

    @ApiModelProperty(value = "友链名")
    private String name;

    @ApiModelProperty(value = "状态")
    private String status;


}
