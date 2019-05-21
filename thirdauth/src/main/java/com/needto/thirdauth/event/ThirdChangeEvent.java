package com.needto.thirdauth.event;

import com.needto.thirdauth.data.ThirdEvent;
import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 * 第三方改变事件
 */
public class ThirdChangeEvent extends ApplicationEvent {
    private ThirdEvent changeData;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ThirdChangeEvent(Object source, ThirdEvent changeData) {
        super(source);
        this.changeData = changeData;
    }
}
