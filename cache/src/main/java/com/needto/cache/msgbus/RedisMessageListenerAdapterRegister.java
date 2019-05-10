package com.needto.cache.msgbus;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisMessageListenerAdapterRegister {

    /**
     * 消息主题
     * @return
     */
    String topic();
}
