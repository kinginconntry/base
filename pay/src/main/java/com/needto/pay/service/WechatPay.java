package com.needto.pay.service;


import com.needto.common.entity.Dict;
import com.needto.common.utils.Assert;
import com.needto.common.utils.CryptoUtil;
import com.needto.common.utils.Utils;
import com.needto.httprequest.service.ApiRequest;
import com.needto.pay.config.WechatOpen;
import com.needto.pay.entity.CallbackData;
import com.needto.pay.entity.Way;
import com.needto.pay.config.WechatConfig;
import com.needto.pay.entity.WechatPayData;
import com.needto.pay.event.PayFailtureEvent;
import com.needto.pay.event.PaySuccessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
@ConditionalOnBean(WechatOpen.class)
public class WechatPay implements Deal<WechatPayData> {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private ApplicationContext applicationContext;

    private void checkParam(WechatPayData payData){
        Assert.validateStringEmpty(payData.getBody(), "NO_BODY", "");
        Assert.validateStringEmpty(payData.getGuid(), "NO_GUID", "no_out_trade_no");
        Assert.validateStringEmpty(payData.getIp(), "NO_IP", "");
        Assert.validateStringEmpty(payData.getTradeType(), "NO_TRADE_TYPE", "");

        Assert.validateCondition(payData.getBigDecimal() == null || payData.getBigDecimal().intValue() <= 0, "ILLEGAL_FEE", "");
        Assert.validateCondition(WechatPayData.TradeType.JSAPI.name().equals(payData.getTradeType()) && StringUtils.isEmpty(payData.getOpenId()), "NO_OPENID", "");
    }

    private String getSign(Dict param){
        List<String> keys = new ArrayList<>(param.keySet());
        keys.sort(String::compareTo);
        StringBuilder stringBuilder = new StringBuilder();
        for(String key : keys){
            stringBuilder.append(key).append("=").append(param.get(key)).append("&");
        }
        return CryptoUtil.Crypto.MD5.encry(stringBuilder.substring(0, stringBuilder.length() - 1), "");
    }

    @Override
    public String prepare(WechatPayData payData) {
        checkParam(payData);
        Dict param = new Dict();
        param.put("appid", wechatConfig.getAppid());
        param.put("much_id", wechatConfig.getMuchId());
        param.put("nonce_str", "" + System.currentTimeMillis());
        param.put("body", payData.getBody());
        if(!StringUtils.isEmpty(payData.getDetail())){
            param.put("detail", payData.getDetail());
        }
        if(!StringUtils.isEmpty(payData.getAttach())){
            param.put("attach", payData.getAttach());
        }
        param.put("out_trade_no", payData.getGuid());
        param.put("total_fee", payData.getBigDecimal().intValue());
        param.put("spbill_create_ip", payData.getIp());
        param.put("notify_url", wechatConfig.getNotifyUrl());
        param.put("trade_type", payData.getTradeType());
        if(!StringUtils.isEmpty(payData.getProductId())){
            param.put("product_id", payData.getProductId());
        }
        if(!StringUtils.isEmpty(payData.getLimitPay())){
            param.put("limit_pay", payData.getLimitPay());
        }
        if(!StringUtils.isEmpty(payData.getOpenId())){
            param.put("openid", payData.getOpenId());
        }

        String sign = getSign(param);
        param.put("sign", sign);
        ResponseEntity<String> rest = ApiRequest.requestString("https://api.mch.weixin.qq.com/pay/unifiedorder", HttpMethod.POST, Utils.objectToXml(param));
        Dict res = new Dict();
        res.putAll(Utils.xmlToJson(rest.getBody()));
        Assert.validateCondition(!"OK".equals(res.get("err_code")), res.getValue("result_code"), res.getValue("err_code_des"));

        if(!StringUtils.isEmpty(res.getValue("code_url"))){
            return res.getValue("code_url");
        }else{
            return res.getValue("prepay_id");
        }
    }

    @Override
    public void payCallback(Dict callback) {
        CallbackData callbackData = new CallbackData();
        callbackData.putAll(callback);
        callbackData.setGuid(callback.getValue("out_trade_no"));
        if("SUCCESS".equals(callback.get("return_code"))){
            applicationContext.publishEvent(new PaySuccessEvent(this, callbackData, getCode()));
        }else{
            applicationContext.publishEvent(new PayFailtureEvent(this, callbackData, getCode()));
        }
    }

    @Override
    public String getCode() {
        return Way.WECHAT.name();
    }
}
