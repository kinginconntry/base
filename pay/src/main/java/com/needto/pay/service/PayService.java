package com.needto.pay.service;

import com.needto.common.exception.BaseException;
import com.needto.common.utils.Assert;
import com.needto.pay.entity.CallbackData;
import com.needto.pay.entity.IPayData;
import com.needto.pay.event.PayFailtureEvent;
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
public class PayService {

    private static final Map<String, Deal> MAP = new HashMap<>();

    private static final Map<String, String> DESC_MAP = new HashMap<>();

    private static final Map<String, String> NAME_MAP = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        Map<String, Deal> filterMap = applicationContext.getBeansOfType(Deal.class);
        filterMap.forEach((k, v) -> set(v.code(), v, v.name(), v.desc()));
    }

    public boolean contain(String code){
        if(StringUtils.isEmpty(code)){
            return false;
        }
        return MAP.containsKey(code);
    }

    public void set(String code, Deal deal, String name, String desc){
        if(StringUtils.isEmpty(code)){
            return;
        }
        Assert.validateCondition(MAP.containsKey(code), "code repeat");
        MAP.put(code, deal);
        NAME_MAP.put(code, name);
        DESC_MAP.put(code, desc);
    }

    public Deal get(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        return MAP.get(code);
    }

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

    public void payCallback(String payWay, CallbackData callbackData){
        Assert.validateCondition(this.contain(payWay), "NO_SUPPORT", "");
        try{
            this.get(payWay).payCallback(callbackData);
        }catch (BaseException e){
            e.printStackTrace();
        }
    }

}
