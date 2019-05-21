package com.needto.chatpush.businesssms;

import com.needto.common.service.ThingContainerService;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class BusinessSmsContainer extends ThingContainerService<ISmsService> {


    @Override
    protected Class<ISmsService> getThingClass() {
        return ISmsService.class;
    }
}
