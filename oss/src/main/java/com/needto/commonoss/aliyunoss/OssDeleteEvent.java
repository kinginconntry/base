package com.needto.commonoss.aliyunoss;

import com.needto.common.entity.Target;
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

    public Target target;

    public OssDeleteEvent(Object source, List<String> keys, Target target) {
        super(source);
        this.keys = keys;
        this.target = target;
    }
}
