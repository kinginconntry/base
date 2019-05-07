package com.needto.common.services.firewall.api;


import java.lang.annotation.*;

/**
 * api接口频率拦截器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ApiFreqLimit {
    /**
     * 可访问最大次数
     * @return
     */
    int max();

    /**
     * 单位时间
     * @return
     */
    int second();

    /**
     * 被禁用时间
     * @return
     */
    int forbidtime() default 600;

    /**
     * 备用方案，是否会使用ip作为客户端识别标识进行拦截
     * @return
     */
    boolean useIp() default true;
    /**
     * 若未检查到客户端时，使用默认的ip拦截，拦截对应的可访问最大次数的放大系数
     * @return
     */
    int times() default 1;
}
