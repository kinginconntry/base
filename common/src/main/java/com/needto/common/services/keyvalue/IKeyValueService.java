package com.needto.common.services.keyvalue;

import com.needto.common.entity.Target;

import java.util.List;

/**
 * @author Administrator
 */
public interface IKeyValueService {

    /**
     * 获取头部信息
     * @return
     */
    KeyValue getName();

    /**
     * 获取结果list
     * @param client
     * @return
     */
    List<KeyValue> getValue(Target client);

    /**
     * 是否为授权应用调用
     * @return
     */
    default boolean isAuth(){
        return true;
    }
}
