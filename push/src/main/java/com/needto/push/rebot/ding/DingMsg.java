package com.needto.push.rebot.ding;


import com.needto.common.entity.IValidate;


/**
 * @author Administrator
 * 钉钉消息
 */
public class DingMsg implements IValidate {

    /**
     * 消息类型
     */
    protected String msgType;

    public DingMsg(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
