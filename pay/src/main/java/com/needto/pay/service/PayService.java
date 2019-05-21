package com.needto.pay.service;

import com.needto.common.entity.Dict;
import com.needto.common.exception.BaseException;
import com.needto.common.service.ThingContainerService;
import com.needto.common.utils.Assert;
import com.needto.pay.entity.IPayData;
import com.needto.pay.event.PayPrepareAfterEvent;
import com.needto.pay.event.PayPrepareBeforeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class PayService extends ThingContainerService<Deal> {


    public String prepare(String payWay, IPayData iPayData){
        Assert.validateCondition(this.contain(payWay), "NO_SUPPORT", "");
        applicationContext.publishEvent(new PayPrepareBeforeEvent(this, iPayData, payWay));
        String link = null;
        try {
            link = this.get(payWay).prepare(iPayData);
            applicationContext.publishEvent(new PayPrepareAfterEvent(this, iPayData, link, payWay));
            return link;
        } catch (BaseException e){
            applicationContext.publishEvent(new PayPrepareAfterEvent(this, iPayData, link, payWay, e.getErrCode()));
            throw e;
        }
    }

    public void payCallback(String payWay, Dict Dict){
        Assert.validateCondition(this.contain(payWay), "NO_SUPPORT", "");
        try{
            this.get(payWay).payCallback(Dict);
        }catch (BaseException e){
            e.printStackTrace();
        }
    }

    @Override
    protected Class<Deal> getThingClass() {
        return Deal.class;
    }
}
