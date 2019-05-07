package com.needto.push.rebot.ding;

import com.needto.common.services.rebot.ding.DingMsg;

import java.util.List;

public class DingActionCardMsg extends DingMsg {

    public static class Btn {
        /**
         * 按钮方案
         */
        public String title;
        /**
         * 点击按钮触发的URL
         */
        public String actionURL;
    }

    public static class ActionCard {
        /**
         * 首屏会话透出的展示内容
         */
        public String title;

        /**
         * markdown格式的消息
         */
        public String text;

        /**
         * 按钮的信息：title-按钮方案，actionURL-点击按钮触发的URL
         */
        public List<Btn> btns;

        /**
         * 0-按钮竖直排列，1-按钮横向排列
         */
        public String btnOrientation;

        /**
         * 0-正常发消息者头像，1-隐藏发消息者头像
         */
        public String hideAvatar;
    }

    public ActionCard actionCard;

    public DingActionCardMsg() {
        super("actionCard");
        this.actionCard = new ActionCard();
    }

    public String getTitle() {
        return this.actionCard.title;
    }

    public void setTitle(String title) {
        this.actionCard.title = title;
    }

    public String getText() {
        return this.actionCard.text;
    }

    public void setText(String text) {
        this.actionCard.text = text;
    }

    public List<Btn> getBtns() {
        return this.actionCard.btns;
    }

    public void setBtns(List<Btn> btns) {
        this.actionCard.btns = btns;
    }

    public String getBtnOrientation() {
        return this.actionCard.btnOrientation;
    }

    public void setBtnOrientation(String btnOrientation) {
        this.actionCard.btnOrientation = btnOrientation;
    }

    public String getHideAvatar() {
        return this.actionCard.hideAvatar;
    }

    public void setHideAvatar(String hideAvatar) {
        this.actionCard.hideAvatar = hideAvatar;
    }
}
