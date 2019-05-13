package com.needto.perm.event;

import com.needto.common.entity.Target;
import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 */
public class TargetRoleChangeEvent extends ApplicationEvent {

    public String owner;

    public Target target;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public TargetRoleChangeEvent(Object source, String owner, Target target) {
        super(source);
        this.owner = owner;
        this.target = target;
    }
}
