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
     * 博客后台redis的登录key前缀
     */
    public static final String ADMIN_LOGIN_KEY_PREFIX =  "adminLogin:";

    public static final String ARTICLE_VIEW_COUNT= "article:viewCount";

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

    public static final int NOT_ARTICLE_VIEW_COUNT = 0;

    /**
     * 权限状态正常
     */
    public static final String PERMISSIONS_STATUS_NORMAL = "0";

    /**
     * 角色状态正常
     */
    public static final String ROLE_STATUS_NORMAL = "0";

    /**
     * 权限类型，菜单
     */
    public static final String TYPE_MENU = "C";

    /**
     * 权限类型，目录
     */
    public static final String TYPE_CATEGORY = "M";

    /**
     * 权限类型，按钮
     */
    public static final String TYPE_BUTTON = "F";

    /**
     * 用户为管理员
     */
    public static final String USER_IS_ADMIN = "admin";

    /**
     * 菜单权限根id
     */
    public static final Long MENU_ROOT_ID = 0L;

    /**
     *  参数最小值为1
     */
    public static final Long VALUE_MIN_NUM = 1L;

    /**
     *  参数为零
     */
    public static final Long VALUE_IS_ZERO = 0L;

    /**
     * 参数为空
     */
    public static final String VALUE_IS_EMPTY = "参数为空";

    /**
     * 分类导出文件名
     */
    public static final String CATEGORY_EXCEL_DIR_NAME = "分类.xlsx";

    /**
     * 判断为管理员用户
     */
    public static final String IS_ADMIN = "1";/**

     /**
     * 判断为管理员用户
     */
    public static final Long IS_SUPER_ADMIN = 1L;

    /**
     * 限制查询十条数据
     */
    public static final String QUERY_LIMIT_NUM_10 = "limit 10";
}