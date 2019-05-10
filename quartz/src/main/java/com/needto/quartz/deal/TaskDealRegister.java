package com.needto.quartz.deal;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TaskDealRegister {

    /**
     * code
     * @return
     */
    String code() default "";

    String name() default "";

    String desc() default "";
}
