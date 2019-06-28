package com.needto.chatpush.email.local.event;

import com.needto.email.entity.EmailData;
import org.springframework.context.ApplicationEvent;

import javax.mail.Session;

/**
 * @author Administrator
 * 邮件发送成功事件
 */
public class EmailAfterSendSuccessEvent extends ApplicationEvent {
    public EmailData emailData;
    public Session session;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public EmailAfterSendSuccessEvent(Object source, EmailData emailData, Session session) {
        super(source);
        this.emailData = emailData;
        this.session = session;
    }

    public EmailData getEmailData() {
        return emailData;
    }

    public void setEmailData(EmailData emailData) {
        this.emailData = emailData;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
