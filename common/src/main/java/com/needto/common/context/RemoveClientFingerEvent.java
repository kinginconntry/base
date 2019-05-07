package com.needto.common.context;

import com.needto.common.entity.Target;
import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 * 移除客户端指纹信息
 */
public class RemoveClientFingerEvent extends ApplicationEvent {

    private Target client;

    public RemoveClientFingerEvent(Object source, Target client) {
        super(source);
        this.client = client;
    }

    public Target getClient() {
        return client;
    }

    public void setClient(Target client) {
        this.client = client;
    }
}
