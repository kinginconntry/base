package com.needto.keyvalue;

import com.needto.tool.entity.Dict;

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
     * @return
     */
    List<KeyValue> getValue(Dict condition);

    default List<KeyValue> getValue(){ return getValue(null); }
}
