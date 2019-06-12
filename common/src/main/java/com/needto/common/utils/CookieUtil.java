package com.needto.common.utils;

import com.needto.common.entity.Cookie;
import com.needto.tool.utils.Utils;

import java.util.List;

public class CookieUtil {
    public static String getSetCookie(List<Cookie> cookieList){
        StringBuilder stringBuilder = new StringBuilder();
        if(cookieList == null){
            return stringBuilder.toString();
        }
        for(Cookie cookie : cookieList){
            stringBuilder.append(cookie.getName()).append("=").append(Utils.nullToString(cookie.getValue())).append(";");
        }
        return stringBuilder.toString();
    }

}
