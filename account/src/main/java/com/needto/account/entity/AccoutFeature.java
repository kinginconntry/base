package com.needto.account.entity;

import com.needto.tool.entity.Dict;

import java.util.Date;

/**
 * @author Administrator
 * 账户特性
 */
public class AccoutFeature {

    /**
     * 特性开始时间
     */
    public Date begin;

    /**
     * 特性结束时间
     */
    public Date end;

    /**
     * 特性值
     */
    public Dict value;

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Dict getValue() {
        return value;
    }

    public void setValue(Dict value) {
        this.value = value;
    }
}
