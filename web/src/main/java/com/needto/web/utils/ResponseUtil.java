package com.needto.web.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletResponse;
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
}
