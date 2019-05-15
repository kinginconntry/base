package com.needto.chatpush.businesssms;

import com.needto.common.utils.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class BusinessSmsContainer {

    private static final Log log = LogFactory.getLog(BusinessSmsContainer.class);

    private static final Map<String, String> DESC_MAP = new HashMap<>();

    private static final Map<String, String> NAME_MAP = new HashMap<>();

    private static final Map<String, ISmsService> MAP = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        Map<String, ISmsService> filterMap = applicationContext.getBeansOfType(ISmsService.class);
        filterMap.forEach((k, v) -> set(v.code(), v, v.name(), v.desc()));
    }

    public boolean contain(String code){
        if(StringUtils.isEmpty(code)){
            return false;
        }
        return MAP.containsKey(code);
    }

    public void set(String code, ISmsService iSmsService, String name, String desc){
        if(StringUtils.isEmpty(code)){
            return;
        }
        Assert.validateCondition(MAP.containsKey(code), "code repeat");
        MAP.put(code, iSmsService);
        NAME_MAP.put(code, name);
        DESC_MAP.put(code, desc);
    }

    public ISmsService get(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        return MAP.get(code);
    }
}
