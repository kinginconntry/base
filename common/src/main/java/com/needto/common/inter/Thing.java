package com.needto.common.inter;

public interface Thing {
    String getName();

    String getCode();

    default String getDesc(){return "";}

    default Object getContent(){return null;}
}
