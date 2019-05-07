package com.needto.common.entity;

/**
 * 系统服务状态
 */
public enum SystemState {
    /**
     * 正在启动
     */
    INITIATING,
    /**
     * 已就绪
     */
    READY,
    /**
     * 已调度重启
     */
    REBOOT_SCHEDULED
}
