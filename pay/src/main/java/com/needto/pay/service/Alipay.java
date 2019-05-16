package com.needto.pay.service;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.needto.common.entity.Dict;
import com.needto.pay.config.AlipayConfig;
import com.needto.pay.entity.AlipayData;
import com.needto.pay.entity.CallbackData;
import com.needto.pay.entity.Way;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnBean(AlipayConfig.class)
public class Alipay implements Deal<AlipayData> {

    private static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

    private static final String RETURN_URL = "";

    @Autowired
    private AlipayConfig alipayConfig;

    private AlipayClient alipayClient;

    @PostConstruct
    private void init(){
        //实例化客户端
        alipayClient = new DefaultAlipayClient(GATEWAY_URL, alipayConfig.getAppId(), alipayConfig.getPrivateKey(), "json", alipayConfig.getCharset(), alipayConfig.getPublicKey(), alipayConfig.getSignType());
    }

    @Override
    public String prepare(AlipayData payData) {

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
        AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
        request.setReturnUrl(alipayConfig.getDomain() + RETURN_URL);
        //SDK已经封装掉了公共参数，这里只需要传入业务参数
        //此次只是参数展示，未进行字符串转义，实际情况下请转义
        request.setBizContent("  {" +
                "    \"primary_industry_name\":\"IT科技/IT软件与服务\"," +
                "    \"primary_industry_code\":\"10001/20102\"," +
                "    \"secondary_industry_code\":\"10001/20102\"," +
                "    \"secondary_industry_name\":\"IT科技/IT软件与服务\"" +
                " }");
        try {
            AlipayOpenPublicTemplateMessageIndustryModifyResponse response = alipayClient.execute(request);
            Dict jsonObject = JSON.parseObject(response.getBody(), Dict.class);
            //调用成功，则处理业务逻辑
            if(response.isSuccess()){

            }else{

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void payCallback(CallbackData result) {

    }

    @Override
    public String code() {
        return Way.ALIPAY.name();
    }
}
