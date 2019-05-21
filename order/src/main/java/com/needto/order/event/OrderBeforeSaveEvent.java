package com.needto.order.event;

import com.needto.common.entity.Target;
import com.needto.order.model.Order;
import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 * 订单保存事件
 */
public class OrderBeforeSaveEvent extends ApplicationEvent {

    public Order order;

    public Target client;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public OrderBeforeSaveEvent(Object source, Order order, Target client) {
        super(source);
        this.order = order;
        this.client = client;
    }
}
