package com.needto.perm.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author Administrator
 */
public class RoleDataChangeEvent extends ApplicationEvent {
    public List<String> roleIds;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RoleDataChangeEvent(Object source, List<String> roleIds) {
        super(source);
        this.roleIds = roleIds;
    }
}
