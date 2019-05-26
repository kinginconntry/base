package com.needto.web.data;


/**
 * @author Administrator
 */
public class Constant {

    /**
     * 内部系统互相调用的url开头
     */
    public static final String INNER_URL_START = "/_/_";

    /**
     * 应用用户调用的url开头
     */
    public static final String APP_URL_START = "/app";

    /**
     * 运营用户调用的url开头
     */
    public static final String ADMIN_URL_START = "/admin";

    /**
     * 系统用户调用的url开头
     */
    public static final String SYS_URL_START = "/sys";

    /**
     * web开放客户端调用的url开头
     */
    public static final String WEB_URL_START = "/web";

    /**
     * api客户端调用的url开头
     */
    public static final String API_URL_START = "/asyncapi";

    /**
     * url签名key
     */
    public static final String SIGN_KEY = "sg";

    /**
     * user-agent key
     */
    public static final String USER_AGENT_HEADER = "User-Agent";

    /**
     * 客户端指纹key
     */
    public static final String FINGER_KEY = "finger";

    /**
     *  客户端类型key
     */
    public static final String CLIENT_TYPE_KEY = "clienttype";

    /**
     * token键
     */
    public static final String TOKEN_KEY = "tk";
}
