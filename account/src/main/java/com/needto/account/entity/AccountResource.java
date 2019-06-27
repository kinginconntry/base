package com.needto.account.entity;

import java.util.Date;

/**
 * @author Administrator
 * 账户拥有资源配置
 */
public class AccountResource {

    /**
     * 开始时间
     */
    public Date start;

    /**
     * 结束时间
     */
    public Date end;

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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
