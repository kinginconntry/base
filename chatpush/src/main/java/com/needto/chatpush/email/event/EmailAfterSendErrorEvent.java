package com.needto.chatpush.email.event;

import org.springframework.context.ApplicationEvent;

import javax.mail.Session;

public class EmailAfterSendErrorEvent extends ApplicationEvent {

    public Session session;
    public String error;
    public String msg;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public EmailAfterSendErrorEvent(Object source, Session session, String error, String msg) {
        super(source);
    }
}
