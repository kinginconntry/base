package com.needto.chatpush;

import com.needto.common.inter.Thing;

/**
 * @author Administrator
 */
public interface IMsgSendService<T> extends Thing {

    /**
     * 发送
     * @param o
     * @return
     */
    boolean send(T o);

    String type();
}
