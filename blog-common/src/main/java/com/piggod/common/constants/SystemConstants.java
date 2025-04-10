package com.piggod.common.constants;

public class SystemConstants {

    /**
     * 文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    /**
     * 文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 文章表内容字段，由于数据量太大一般不需要返回
     */
    public static final String ARTICLE_CONTENT_FIELD = "content";


    /**
     * 分类是正常状态
     */
    public static final String CATEGORY_STATUS_NORMAL = "0";

    /**
     * 数据字段 - create_time
     */
    public static final String DATA_FIELD_NAME_CREATE_TIME = "create_time";

    /**
     * 分页查询首页
     */
    public static final Integer PAGE_QUERY_FIRST = 1;

    /**
     * 友链状态为审核通过
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * 博客前台redis的登录key前缀
     */
    public static final String BLOG_LOGIN_KEY_PREFIX =  "blogLogin:";

    /**
     * 评论为-1代表是文章的根评论
     */
    public static final Long ARTICLE_COMMENT_ROOT_ID = -1L;

    /**
     * 用户已经注销或者查不到昵称
     */
    public static final String USER_CANCELED = "用户已注销";

    /**
     * 文章的评论
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 友链的评论
     */
    public static final String LINK_COMMENT = "1";
}