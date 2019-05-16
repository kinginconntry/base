package com.needto.pay.service;


import com.needto.pay.entity.CallbackData;
import com.needto.pay.entity.Way;
import com.needto.pay.config.WechatConfig;
import com.needto.pay.entity.WechatPayData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnBean(WechatConfig.class)
public class WechatPay implements Deal<WechatPayData> {

    @Autowired
    private WechatConfig wechatConfig;

    @PostConstruct
    private void init(){

    }

    @Override
    public String prepare(WechatPayData payData) {
        return null;
    }

    @Override
    public void payCallback(CallbackData callback) {

    }

    @Override
    public String code() {
        return Way.WECHAT.name();
    }
}
