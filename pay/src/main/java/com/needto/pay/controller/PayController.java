package com.needto.pay.controller;


import com.needto.pay.entity.Way;
import com.needto.pay.service.PayService;
import com.needto.tool.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping("/pay/alipay/notify")
    public void alipayCallback(@RequestBody Dict param){
        payService.payCallback(Way.ALIPAY.name(), param);
    }

    @RequestMapping("/pay/wechat/notify")
    public void wechatCallback(@RequestBody Dict param){
        payService.payCallback(Way.WECHAT.name(), param);
    }

}
