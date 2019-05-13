package com.needto.perm.data;

import java.lang.annotation.*;

/**
 * 功能权限校验
 * 用于controller层，拦截方法
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface PermAuth {

    /**
     * 检查
     * @return
     */
    String value() default "";

    /**
     * 失败时的错误码
     * @return
     */
    String errcode() default "";

    /**
     * 失败时的错误消息
     * @return
     */
    String msg() default "";
}
