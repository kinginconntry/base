package com.needto.common.entity;

/**
 * 系统服务状态
 * @author Administrator
 */
public enum SystemState {
    /**
     * 容器未开始
     */
    NO,
    /**
     * 正在启动
     */
    INITIATING,
    /**
     * 已停止
     */
    STOP,
    /**
     * 运行中
     */
    RUNNING;
}
