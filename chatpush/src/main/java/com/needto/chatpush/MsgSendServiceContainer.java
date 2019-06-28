package com.needto.chatpush;

import com.needto.chatpush.sms.ISmsService;
import com.needto.common.service.ThingContainerService;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class MsgSendServiceContainer extends ThingContainerService<IMsgSendService> {


    @Override
    protected Class<IMsgSendService> getThingClass() {
        return IMsgSendService.class;
    }
}
