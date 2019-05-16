package com.needto.pay.event;

import com.needto.pay.entity.CallbackData;
import org.springframework.context.ApplicationEvent;

public class PayCancelEvent extends ApplicationEvent {
    private String payWay;
    private CallbackData callbackData;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public PayCancelEvent(Object source, CallbackData callbackData, String payWay) {
        super(source);
        this.callbackData = callbackData;
        this.payWay = payWay;
    }

    public CallbackData getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(CallbackData callbackData) {
        this.callbackData = callbackData;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
}
