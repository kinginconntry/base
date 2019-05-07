package com.needto.common.entity;

/**
 * @author Administrator
 * 实体类校验接口
 */
public interface IValidate {

    /**
     * 检查实体类是否数据合法
     * @return
     */
    default boolean validate(){ return false; }
}
