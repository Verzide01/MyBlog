package com.piggod.common.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class UserInfoVO {

    /**
     * 主键
     */
    private Long id; //  要不要删除

    /**
     * 昵称
     */
    private String nickName;

    private String userName;

    private String status;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;


}
