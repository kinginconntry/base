package com.needto.commonoss.aliyunoss;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author Administrator
 * oss链接删除
 */
public class OssDeleteEvent extends ApplicationEvent {

    /**
     * 被删除的链接
     */
    public List<String> keys;

    public OssDeleteEvent(Object source, List<String> keys) {
        super(source);
        this.keys = keys;
    }
}
