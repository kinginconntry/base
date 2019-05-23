package com.needto.common.inter;

public interface Ip {
    default void setIp(String ip){}

    default String getIp(){ return ""; }
}
