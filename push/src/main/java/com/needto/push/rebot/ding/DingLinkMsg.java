package com.needto.push.rebot.ding;

import com.needto.common.services.rebot.ding.DingMsg;

/**
 * @author Administrator
 * link消息
 */
public class DingLinkMsg extends DingMsg {

    public static class Link {
        /**
         * 消息标题
         */
        public String title;

        /**
         * 消息内容。如果太长只会部分展示
         */
        public String text;

        /**
         * 点击消息跳转的URL
         */
        public String messageUrl;

        /**
         * 图片URL
         */
        public String picUrl;
    }

    public Link link;

    public DingLinkMsg() {
        super("link");
        this.link = new Link();
    }

    public String getTitle() {
        return this.link.title;
    }

    public void setTitle(String title) {
        this.link.title = title;
    }

    public String getText() {
        return this.link.text;
    }

    public void setText(String text) {
        this.link.text = text;
    }

    public String getMessageUrl() {
        return this.link.messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.link.messageUrl = messageUrl;
    }

    public String getPicUrl() {
        return this.link.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.link.picUrl = picUrl;
    }
}
