package com.needto.chatpush.sms;

import com.needto.chatpush.IMsgSendService;

/**
 * @author Administrator
 */
public interface ISmsService extends IMsgSendService {

    String TYPE = "SMS";

    @Override
    default String type(){
        return TYPE;
    }
}
