package com.needto.chatpush.email.entity;

import javax.mail.internet.MimeBodyPart;

/**
 * @author Administrator
 * 正文
 */
public class Content implements IBodyBuilder {

    public String content;

    public String type;

    public Content() {
        this.type = "text/html;charset=UTF-8";
    }

    @Override
    public MimeBodyPart builder() {
        return null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
