package com.piggod.common.enums;

import lombok.Getter;

@Getter
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    REQUIRE_PASSWORD(504,"必须填写密码"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),
    SAVE_UNSUCCESS(507, "保存失败"),
    NOT_CHANGE_INFO(508, "请更改信息"),
    FILE_IS_NULL(509, "文件不能为空，请上传文件"),
    FILE_TYPE_ERROR(510,  "文件格式错误，请重新上传为jpg、jpeg或png格式的文件"),
    REGISTER_NOT_NULL(511, "注册信息不能为空"),
    NICKNAME_EXIST(512, "昵称已经存在"),
    USER_EXIST(513, "用户已经存在"),
    LOGIN_INFO_NOT_NULL(514, "请输入用户名和密码"),
    PARAM_INVALID(515, "异常传参"),
    QUERY_IS_NULL(516, "查询内容为空，请输入内容再试");
    private final int code;
    private final String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

}