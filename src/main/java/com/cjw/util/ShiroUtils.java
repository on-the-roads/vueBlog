package com.cjw.util;

import com.cjw.shiro.UiUser;
import org.apache.shiro.SecurityUtils;

public class ShiroUtils {
    //  token中获取userID
    public static Long getUserId() {
        return ((UiUser) SecurityUtils.getSubject().getPrincipal()).getId();
    }
}
