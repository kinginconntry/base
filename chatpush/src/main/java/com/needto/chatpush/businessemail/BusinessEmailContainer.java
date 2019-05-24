package com.needto.chatpush.businessemail;

import com.needto.common.service.ThingContainerService;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class BusinessEmailContainer extends ThingContainerService<IEmailService> {

    @Override
    protected Class<IEmailService> getThingClass() {
        return IEmailService.class;
    }
}
