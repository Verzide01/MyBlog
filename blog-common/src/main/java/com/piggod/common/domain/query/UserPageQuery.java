package com.piggod.common.domain.query;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "用户分页查询条件")
public class UserPageQuery extends PageQuery {

    private String userName;

    private String phonenumber;

    private String status;
}
