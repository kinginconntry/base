package com.needto.thirdauth.controller;

import com.needto.thirdauth.data.ThirdEvent;
import com.needto.thirdauth.service.ThirdService;
import com.needto.tool.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Administrator
 * 第三方授权通用
 */
@RestController
public class ThirdAuthController {

    @Autowired
    private ThirdService thirdService;

    /**
     * 事件
     * @param param
     */
    @RequestMapping("/third/event/{type}")
    public void event(@PathVariable String type, @RequestBody Dict param){

        thirdService.event(new ThirdEvent(type, param));
    }
}
