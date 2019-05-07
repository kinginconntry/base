package com.needto.services.user.ownercommon.entity;

/**
 * @author Administrator
 * 账户拥有资源配置
 */
public class AccountResource {

    /**
     * 当前可用资源数
     */
    public int current;

    /**
     * 总资源数
     */
    public int total;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
