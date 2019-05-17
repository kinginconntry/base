package com.needto.notice.entity;

/**
 * @author Administrator
 * 系统消息实际数据实体
 */
public class NoticeMsg {

    /**
     * 消息类型
     */
    protected String type;

    /**
     * 消息分组
     */
    protected String group;

    public String getType(){
        return this.type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
