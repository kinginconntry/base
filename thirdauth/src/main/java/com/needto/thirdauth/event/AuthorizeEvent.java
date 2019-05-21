package com.needto.thirdauth.event;

import com.needto.thirdauth.data.ThirdAuth;
import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 * 第三方授权事件
 */
public class AuthorizeEvent extends ApplicationEvent {
    private ThirdAuth thirdAuth;
    private String userId;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AuthorizeEvent(Object source, ThirdAuth thirdAuth, String userId) {
        super(source);
        this.userId = userId;
        this.thirdAuth = thirdAuth;
    }

    public ThirdAuth getThirdAuth() {
        return thirdAuth;
    }

    public void setThirdAuth(ThirdAuth thirdAuth) {
        this.thirdAuth = thirdAuth;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
