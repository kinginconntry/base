package com.needto.web.utils;

import com.alibaba.fastjson.JSON;
import com.needto.common.entity.Target;
import com.needto.web.data.Constant;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 * Ajax json输出
 */
public class ResponseUtil {

    public static void outJson(ServletResponse response, Object object) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(object));
        writer.close();
    }

    /**
     * 设置客户端指纹
     * @param response
     * @param finger
     */
    public static void setFingerPrint(ServletResponse response, Target finger){
        HttpServletResponse httpServletResponse = ((HttpServletResponse)response);

        Cookie cookieFinger = CookieUtils.createCookie(Constant.FINGER_KEY, finger.getGuid(), "/", -1);
        Cookie cookieFingerType = CookieUtils.createCookie(Constant.CLIENT_TYPE_KEY, finger.getType(), "/", -1);
        httpServletResponse.addCookie(cookieFinger);
        httpServletResponse.addCookie(cookieFingerType);

        httpServletResponse.setHeader(Constant.FINGER_KEY, finger.getGuid());
        httpServletResponse.setHeader(Constant.CLIENT_TYPE_KEY, finger.getType());
    }


}
