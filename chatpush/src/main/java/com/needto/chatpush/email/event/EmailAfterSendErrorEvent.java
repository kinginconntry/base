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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
