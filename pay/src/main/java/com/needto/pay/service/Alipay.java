package com.needto.pay.service;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.needto.pay.config.AlipayConfig;
import com.needto.pay.config.AlipayOpen;
import com.needto.pay.entity.AlipayData;
import com.needto.pay.entity.CallbackData;
import com.needto.pay.entity.Way;
import com.needto.pay.event.PayFailtureEvent;
import com.needto.pay.event.PaySuccessEvent;
import com.needto.tool.entity.Dict;
import com.needto.tool.exception.LogicException;
import com.needto.tool.utils.Assert;
import com.needto.tool.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;

/**
 */
@Component
@ConditionalOnBean(AlipayOpen.class)
public class Alipay implements Deal<AlipayData> {

    private static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private ApplicationContext applicationContext;

    private AlipayClient alipayClient;

    @PostConstruct
    private void init(){
        //实例化客户端
        alipayClient = new DefaultAlipayClient(GATEWAY_URL, alipayConfig.getAppId(), alipayConfig.getPrivateKey(), "json", "utf-8", alipayConfig.getPublicKey(), "RSA2");
    }

    private void checkParam(AlipayData payData){
        Assert.validateStringEmpty(payData.getGuid(), "NO_GUID", "");
        Assert.validateStringEmpty(payData.getProductCode(), "NO_PRODUCT_CODE", "");
        Assert.validateStringEmpty(payData.getSubject(), "NO_SUBJECT", "");
        Assert.validateCondition(payData.getBigDecimal() == null || payData.getBigDecimal().intValue() <= 0, "ILLEGAL_FEE", "");
    }

    private void validateSign(String sign){

    }

    @Override
    public String prepare(AlipayData payData) {
        checkParam(payData);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
        AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setApiVersion("1.0");
        Dict bizContent = new Dict();
        bizContent.put("out_trade_no", payData.getGuid());
        bizContent.put("product_code", payData.getProductCode());
        bizContent.put("subject", payData.getSubject());
        bizContent.put("body", payData.getBody());
        bizContent.put("total_amount", payData.getBigDecimal().divide(BigDecimal.valueOf(100)).intValue());
        bizContent.put("integration_type", payData.getIntegrationType());
        request.setBizContent(JSON.toJSONString(bizContent));
        try {
            AlipayOpenPublicTemplateMessageIndustryModifyResponse response = alipayClient.execute(request);
            Dict res = JSON.parseObject(response.getBody(), Dict.class);
            //调用成功，则处理业务逻辑
            if(response.isSuccess()){
                Map<String, Object> payRes = res.getValue("alipay_trade_page_pay_response");
                Assert.validateCondition(!"Success".equals(payRes.get("msg")), Utils.nullToString(payRes.get("sub_code")), Utils.nullToString(payRes.get("sub_msg")));
                return "";
            }else{
                throw new LogicException("-1", "");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void payCallback(Dict callback) {
        CallbackData callbackData = new CallbackData();
        callbackData.putAll(callback);
        callbackData.setGuid(callback.getValue("out_trade_no"));
        if("TRADE_SUCCESS".equals(callback.get("trade_status"))){
            applicationContext.publishEvent(new PaySuccessEvent(this, callbackData, getCode()));
        }else{
            applicationContext.publishEvent(new PayFailtureEvent(this, callbackData, getCode()));
        }
    }

    @Override
    public String getCode() {
        return Way.ALIPAY.name();
    }
}
