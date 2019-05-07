package com.needto.push.rebot.ding;


/**
 * @author Administrator
 */
public class DingWholeActionCardMsg extends DingMsg {

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
         * 单个按钮的方案。(设置此项和singleURL后btns无效)
         */
        public String singleTitle;

        /**
         * 点击singleTitle按钮触发的URL
         */
        public String singleURL;

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

    public DingWholeActionCardMsg() {
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

    public String getSingleTitle() {
        return this.actionCard.singleTitle;
    }

    public void setSingleTitle(String singleTitle) {
        this.actionCard.singleTitle = singleTitle;
    }

    public String getSingleURL() {
        return this.actionCard.singleURL;
    }

    public void setSingleURL(String singleURL) {
        this.actionCard.singleURL = singleURL;
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
