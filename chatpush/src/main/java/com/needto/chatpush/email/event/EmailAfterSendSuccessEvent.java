package com.needto.chatpush.email.event;

import com.needto.chatpush.email.entity.EmailData;
import org.springframework.context.ApplicationEvent;

import javax.mail.Session;

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
}