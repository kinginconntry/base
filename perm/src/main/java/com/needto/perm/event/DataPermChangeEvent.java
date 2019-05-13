package com.needto.perm.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author Administrator
 */
public class DataPermChangeEvent extends ApplicationEvent {

    public List<String> dataPermIds;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DataPermChangeEvent(Object source, List<String> dataPermIds) {
        super(source);
        this.dataPermIds = dataPermIds;
    }
}
