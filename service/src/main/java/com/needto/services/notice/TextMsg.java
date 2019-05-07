package com.needto.services.notice;

import com.needto.common.services.notice.NoticeMsg;

/**
 * @author Administrator
 * 文本消息
 */
public class TextMsg implements NoticeMsg {

    protected String type = "TEXT";

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
