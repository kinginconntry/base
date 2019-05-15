package com.needto.chatpush;

/**
 * @author Administrator
 */
public interface IMsgSendService<T> {

    /**
     * 发送
     * @param o
     * @return
     */
    boolean send(T o);

    /**
     * 名称
     * @return
     */
    default String name(){ return ""; }

    /**
     * 描述
     * @return
     */
    default String desc(){ return ""; }

    /**
     * 唯一代号
     * @return
     */
    String code();
}
