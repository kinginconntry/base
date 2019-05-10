package com.needto.keyvalue;

import com.needto.common.entity.Target;

import java.util.List;

/**
 * @author Administrator
 */
public interface IKeyValueService {

    /**
     * 分组
     * @return
     */
    String getGroup();

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
}
