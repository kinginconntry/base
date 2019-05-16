package com.needto.pay.event;

import com.needto.pay.entity.IPayData;
import org.springframework.context.ApplicationEvent;

public class PayPrepareBeforeEvent extends ApplicationEvent {
    private String payWay;
    private IPayData iPayData;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public PayPrepareBeforeEvent(Object source, IPayData iPayData, String payWay) {
        super(source);
        this.iPayData = iPayData;
        this.payWay = payWay;
    }

    public IPayData getiPayData() {
        return iPayData;
    }

    public void setiPayData(IPayData iPayData) {
        this.iPayData = iPayData;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
}
