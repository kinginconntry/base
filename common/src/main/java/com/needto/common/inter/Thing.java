package com.needto.common.inter;

/**
 * @author Administrator
 * 现实事物接口
 */
public interface Thing {
    /**
     * 事物名
     * @return
     */
    default String getName(){ return ""; }

    /**
     * 事物唯一编码
     * @return
     */
    String getCode();

    /**
     * 事物描述
     * @return
     */
    default String getDesc(){return "";}

    /**
     * 事物内容
     * @return
     */
    default Object getContent(){return null;}
}
