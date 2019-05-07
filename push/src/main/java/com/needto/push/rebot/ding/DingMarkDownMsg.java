package com.needto.push.rebot.ding;


import java.util.List;

public class DingMarkDownMsg extends DingMsg {

    public static class MarkDown {
        /**
         * markdown格式的消息
         */
        public String text;

        /**
         * 首屏会话透出的展示内容
         */
        public String title;
    }

    public MarkDown markdown;

    public At at;

    public DingMarkDownMsg() {
        super("markdown");
        this.markdown = new MarkDown();
        this.at = new At();
    }

    public String getText() {
        return this.markdown.text;
    }

    public void setText(String text) {
        this.markdown.text = text;
    }

    public String getTitle() {
        return this.markdown.title;
    }

    public void setTitle(String title) {
        this.markdown.title = title;
    }

    public List<String> getAtMobiles() {
        return this.at.atMobiles;
    }

    public void setAtMobiles(List<String> atMobiles) {
        this.at.atMobiles = atMobiles;
    }

    public boolean isAtAll() {
        return this.at.isAtAll;
    }

    public void setAtAll(boolean atAll) {
        this.at.isAtAll = atAll;
    }
}
