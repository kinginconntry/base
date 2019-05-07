package com.needto.services.notice;

import com.needto.common.entity.Target;
import com.needto.dao.models.BaseEntity;

/**
 * @author Administrator
 */
public class TargetVersion extends BaseEntity {

    public static final String TABLE = "_targetVersion";

    /**
     * 类型
     * @see Notice.Type
     */
    private String type;

    /**
     * （目标）接受者
     */
    private Target target;

    /**
     * 对象对比当前的版本拉取站内消息（拉取所有高于目标当前版本的消息）
     */
    private long version;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
