package com.piggod.common.utils;

import com.piggod.common.domain.po.LoginUser;
import com.piggod.common.domain.po.User;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//在'发送评论'功能那里会用到的工具类
public class SecurityUtils {

    /**
     * 获取用户的userid
     **/
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        if (authentication.getPrincipal() instanceof LoginUser) {
            // 如果类型不是LoginUser类型说明还没登陆
            return (LoginUser) authentication.getPrincipal();
        }
        else{
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 指定userid为1的用户就是网站管理员
     * @return
     */
    public static Boolean isAdmin(){
        LoginUser loginUser = getLoginUser();
        if(loginUser == null){
            return false;
        }
        Long id = loginUser.getUser().getId();
        return id != null && Long.valueOf(1L).equals(id) ;
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if(loginUser == null){
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        return loginUser.getUser().getId();
    }
}