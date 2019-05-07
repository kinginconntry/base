package com.needto.common.entity;

import com.needto.common.dao.models.BaseEntity;

/**
 * @author Administrator
 * 用户事件配置基类
 */
public class UserEventConfig extends BaseEntity {

    /**
     * 用户主账户id
     */
    public String owner;

    /**
     * 发生事件
     */
    public String event;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
