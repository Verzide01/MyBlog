package com.piggod.common.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章标签关联表
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog_article_tag")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;


}
