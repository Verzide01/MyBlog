package com.piggod.common.domain.dto;

import com.piggod.common.domain.vo.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode()
@Data
@NotNull(message = "内容不能为空")
public class UserInfoDTO  {
    /**
     * 主键
     */

    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

}
