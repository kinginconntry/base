package com.needto.notice.event;

import com.google.common.collect.Lists;
import com.needto.common.entity.Target;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author Administrator
 * 通知改变事件
 */
public class NoticeChangeEvent extends ApplicationEvent {

    public List<Target> targets;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public NoticeChangeEvent(Object source, List<Target> targets) {
        super(source);
        this.targets = targets;
    }

    public NoticeChangeEvent(Object source, Target target) {
        super(source);
        this.targets = Lists.newArrayList(target);
    }
}
