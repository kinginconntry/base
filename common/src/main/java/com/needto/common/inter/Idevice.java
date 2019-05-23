package com.needto.common.inter;

/**
 * @author Administrator
 * 设备
 */
public interface Idevice {

    default void setDevice(String device){}

    default String getDevice(){ return ""; }
}
