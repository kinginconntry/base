package com.needto.chatpush.email.entity;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 邮件数据
 */
public class EmailData {

    /**
     * 邮件头
     */
    private Map<String, String> headers;
    /**
     * 发件人
     */
    private String from;
    /**
     * 接收人
     */
    private List<String> to;
    /**
     * 抄送
     */
    private List<String> cc;
    /**
     * 密送
     */
    private List<String> bcc;

    /**
     * 主题
     */
    private String subject;

    /**
     * 正文
     */
    private Content content;

    /**
     * 正文使用的图片
     */
    private List<IBodyBuilder> images;
    /**
     * 跟正文相关的附件
     */
    private List<IBodyBuilder> attachs;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public List<IBodyBuilder> getImages() {
        return images;
    }

    public void setImages(List<IBodyBuilder> images) {
        this.images = images;
    }

    public List<IBodyBuilder> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<IBodyBuilder> attachs) {
        this.attachs = attachs;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
