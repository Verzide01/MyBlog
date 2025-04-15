package com.piggod.common.domain.dto;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {


    /**
     * 评论类型（0代表文章评论，1代表友链评论）
     */
    @NotEmpty(message = "评论类型不能为空")
    private String type;

    /**
     * 文章id
     */
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    /**
     * 根评论id
     */
    @NotNull(message = "根评论id不能为空")
    private Long rootId;

    /**
     * 评论内容
     */
    @NotEmpty(message = "评论内容不能为空")
    private String content;

    /**
     * 所回复的目标评论的userid
     */
    @NotNull(message = "目标评论的userid不能为空")
    private Long toCommentUserId;

    /**
     * 回复目标评论id
     */
    @NotNull(message = "回复目标评论id不能为空")
    private Long toCommentId;

//    private Long createBy;

}
