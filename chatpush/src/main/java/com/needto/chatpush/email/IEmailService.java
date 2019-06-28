package com.needto.chatpush.email;

import com.needto.chatpush.IMsgSendService;

/**
 * @author Administrator
 */
public interface IEmailService<T> extends IMsgSendService<T> {

    String TYPE = "email";

    @Override
    default String type(){ return TYPE; }
}
