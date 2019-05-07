package com.needto.common.services.eventbus.distribute;

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
