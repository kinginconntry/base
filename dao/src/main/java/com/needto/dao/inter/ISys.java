package com.needto.dao.inter;

public interface ISys {

    /**
     * 创建者或更新者为系统时配置的数据
     */
    String SYS = "__SYS__";

    default void setSys(String sys){}
    default String getSys(){ return SYS; }
}
