package com.needto.chatpush.email.entity;


import javax.mail.internet.MimeBodyPart;

/**
 * @author Administrator
 * 邮件内容构建器
 */
public interface IBodyBuilder {

    /**
     * 构建内容
     * @return
     */
    MimeBodyPart builder();
}
