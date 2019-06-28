package com.needto.chatpush;

import com.needto.tool.entity.Dict;
import com.needto.tool.inter.Thing;

import java.util.List;

/**
 * @author Administrator
 */
public interface IMsgSendService<T> extends Thing {

    /**
     * 发送单个
     * @param o
     * @return
     */
    boolean send(T o, Dict config);

    default boolean send(T o){
        return send(o, null);
    }

    /**
     * 发送多个
     * @param obj
     * @param config
     * @return
     */
    default boolean send(List<T> obj, Dict config){
        for(T temp : obj){
            this.send(temp, config);
        }
        return true;
    }

    default boolean send(List<T> obj){
        return send(obj, null);
    }


    /**
     * 类型
     * @return
     */
    default String type(){ return "MSG"; }
}
