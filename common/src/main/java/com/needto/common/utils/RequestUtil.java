package com.needto.common.utils;

import com.needto.common.entity.ClientType;
import com.needto.common.data.Constant;
import com.needto.common.entity.Target;
import com.needto.tool.exception.ValidateException;
import com.needto.tool.utils.Assert;
import com.needto.tool.utils.SignUtils;
import com.needto.tool.utils.Utils;
import com.needto.tool.utils.ValidateUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author Administrator
 */
public class RequestUtil {

    /**
     * 获取url参数
     *
     * @param url url
     * @return 参数map（null表示url有问题）
     */
    public static Map<String, Object> getUrlParam(String url) {
        if (!ValidateUtils.isUrl(url)) {
            throw new ValidateException("", "url is not be fit");
        }
        Map<String, Object> map = new HashMap<>();
        int index = url.indexOf("?");
        if (index > 0) {
            String param = url.substring(index + 1);
            String[] paramArray = param.split("&");
            String temp;
            for (int i = 0, len = paramArray.length; i < len; i++) {
                temp = paramArray[i];
                int eqIndex = temp.indexOf("=");
                if (eqIndex > -1) {
                    map.put(temp.substring(0, eqIndex), temp.substring(eqIndex + 1));
                }
            }
        }
        return map;
    }

    /**
     * 验证请求是否被签名
     *
     * @param request 请求对象
     * @param secret  密钥
     * @return 是否被签名
     */
    public static boolean validatRequestSign(ServletRequest request, String secret) {
        return SignUtils.validateParamSign(getRequestParam(request), secret);
    }

    /***
     * 获取 request 中 参数
     *
     * @param request 请求对象
     * @return : 参数json（null表示参数不对）
     */
    public static Map<String, Object> getRequestParam(ServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, request.getParameter(key));
        }
        return map;
    }

    /**
     * 获取内部签名值
     * @param request
     * @return
     */
    public static String getInnerServiceSign(HttpServletRequest request){
        Map<String, Object> map = getRequestParam(request);
        if(!StringUtils.isEmpty(map.get(Constant.SIGN_KEY))){
            return map.get(Constant.SIGN_KEY).toString();
        }
        return request.getHeader(Constant.SIGN_KEY);
    }

    /**
     * 判断是否为ajax
     *
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if (StringUtils.isEmpty(requestType) || !"XMLHTTPREQUEST".equals(requestType.toUpperCase())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 编码url
     *
     * @param redirect
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeUrl(String redirect) throws UnsupportedEncodingException {
        return URLEncoder.encode(redirect, "utf-8");
    }

    /**
     * 给url添加token信息
     *
     * @param url
     * @param token
     * @return
     */
    public static String setUrlToken(String url, String token) {
        if (ValidateUtils.isUrl(url)) {
            throw new ValidateException("", "illegal url");
        }
        if (url.contains("?")) {
            url = String.format("%s&%s=%s", url, Constant.TOKEN_KEY, token);
        } else {
            url = String.format("%s?%s=%s", url, Constant.TOKEN_KEY, token);
        }
        return url;
    }

    /**
     * 从请求中获取token
     * token可以再url参数中，头部或cookie
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        Map<String, Object> param = getRequestParam(request);
        String token = Utils.nullToString(param.get(Constant.TOKEN_KEY));
        if (!StringUtils.isEmpty(token)) {
            return token;
        }
        token = CookieUtils.getCookieValue(request, Constant.TOKEN_KEY);
        if (!StringUtils.isEmpty(token)) {
            return token;
        }
        return request.getHeader(Constant.TOKEN_KEY);
    }

    /**
     * 获取请求的来源ip地址，根据X-Real-IP头部、X-Forwarded-For头部以及remoteAddr
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        try {
            String header = Utils.trim(request.getHeader("X-Real-IP"));
            if (!StringUtils.isEmpty(header)) {
                return header;
            }
            header = Utils.trim(request.getHeader("X-Forwarded-For"));
            if (!StringUtils.isEmpty(header)) {
                String[] ss = header.split(",");
                if (ss.length > 0) {
                    return ss[0].trim();
                }
                return header;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return request.getRemoteAddr();
    }

    /**
     * 获取客户端ua
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return Utils.nullToString(request.getHeader(Constant.USER_AGENT_HEADER)).toLowerCase();
    }

    /**
     * 获取客户端指纹
     * 1 先获取token
     * 2 若都没有就获取客户端生成的key
     * 3 若都没有则根据请求特性生成指纹信息(ip + ua + 其他信息)
     * @param request
     * @return
     */
    public static Target getRequestFingerPrint(HttpServletRequest request){
        Map<String, Object> param = getRequestParam(request);

        /**
         * 获取用户token指纹
         */
        String fingerPrint = Utils.nullToString(param.get(Constant.TOKEN_KEY));
        if (!StringUtils.isEmpty(fingerPrint)) {
            return new Target(ClientType.TOKEN.name(), fingerPrint);
        }
        fingerPrint = CookieUtils.getCookieValue(request, Constant.TOKEN_KEY);
        if(!StringUtils.isEmpty(fingerPrint)){
            return new Target(ClientType.TOKEN.name(), fingerPrint);
        }
        fingerPrint = request.getHeader(Constant.TOKEN_KEY);
        if(!StringUtils.isEmpty(fingerPrint)){
            return new Target(ClientType.TOKEN.name(), fingerPrint);
        }

        // 获取设置的客户端指纹信息
        fingerPrint = request.getHeader(Constant.FINGER_KEY);
        if(!StringUtils.isEmpty(fingerPrint)){
            String type = request.getHeader(Constant.CLIENT_TYPE_KEY);
            Assert.validateCondition(!ClientType.contain(type), "UNKNOW_CLIENT");
            return new Target(type, fingerPrint);
        }

        // 获取cookie中的指纹信息
        fingerPrint = CookieUtils.getCookieValue(request, Constant.FINGER_KEY);
        if(!StringUtils.isEmpty(fingerPrint)){
            String type = CookieUtils.getCookieValue(request, Constant.CLIENT_TYPE_KEY);
            Assert.validateCondition(!ClientType.contain(type), "UNKNOW_CLIENT");
            return new Target(type, fingerPrint);
        }

        // 没有则产生一个指纹信息
        return new Target(ClientType.NO_AUTH.name(), Utils.getGuid());
    }

    /**
     * 判断url以什么字符串开始
     * @param url
     * @param start
     * @return
     */
    public static boolean urlType(String url, String start){
        if(StringUtils.isEmpty(url)){
            return false;
        }
        return url.startsWith(start);
    }

    public static void main(String[] args) {
        String url = "http://gadg:80/aa/b/";
        String secret = "a";
        String signUrl = SignUtils.signUrl(url, "", secret);
        System.out.println(signUrl);
        System.out.println(SignUtils.validateUrlSign(signUrl, null, secret));
    }
}
