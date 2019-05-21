package com.needto.thirdauth.service;

import com.needto.common.service.ThingContainerService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class AuthRequestContainer extends ThingContainerService<IAuth> {

    @Override
    protected Class<IAuth> getThingClass() {
        return IAuth.class;
    }
}
