package com.needto.pay.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PayController {

    @RequestMapping
    @ResponseBody
    public void alipayCallback(HttpServletRequest request){

    }

}
