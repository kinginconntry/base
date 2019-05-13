package com.needto.chatpush.ding;


import java.util.List;

/**
 * @author Administrator
 * 文本消息
 */
public class DingTextMsg extends DingMsg {

    public static class Text {
        /**
         * 消息内容
         */
        public String content;
    }

    public Text text;

    public At at;

    public DingTextMsg() {
        super("text");
        this.text = new Text();
        this.at = new At();
    }

    public String getContent() {
        return this.text.content;
    }

    public void setContent(String content) {
        this.text.content = content;
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
