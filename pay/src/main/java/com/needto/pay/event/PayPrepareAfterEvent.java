package com.needto.pay.event;

import com.needto.pay.entity.IPayData;
import org.springframework.context.ApplicationEvent;

public class PayPrepareAfterEvent extends ApplicationEvent {
    private String payWay;
    private IPayData iPayData;
    private String link;
    private String errcode;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public PayPrepareAfterEvent(Object source, IPayData iPayData, String link, String payWay) {
        super(source);
        this.iPayData = iPayData;
        this.link = link;
        this.payWay = payWay;
    }

    public PayPrepareAfterEvent(Object source, IPayData iPayData, String link, String payWay, String errcode) {
        super(source);
        this.iPayData = iPayData;
        this.link = link;
        this.payWay = payWay;
        this.errcode = errcode;
    }

    public IPayData getiPayData() {
        return iPayData;
    }

    public void setiPayData(IPayData iPayData) {
        this.iPayData = iPayData;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }
}
