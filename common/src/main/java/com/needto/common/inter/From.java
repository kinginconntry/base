package com.needto.common.inter;

/**
 * @author Administrator
 * 来自于哪里
 */
public interface From {
    default void setFrom(String from){}
    default String getFrom(){ return ""; }
}
