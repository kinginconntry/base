package com.needto.dao.models;

import com.needto.common.entity.Target;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 * 跟目标相关的类
 */
public class TargetEntity extends BaseEntity {

    /**
     * 创建者字段
     */
    public static final transient String CTARGET = "ctarget";

    /**
     * 更新者字段
     */
    public static final transient String UPTARGET = "uptarget";

    /**
     * 创建者
     */
    protected Target ctarget;

    /**
     * 更新者
     */
    protected Target uptarget;

    public Target getCtarget() {
        return ctarget;
    }

    public void setCtarget(Target ctarget) {
        this.ctarget = ctarget;
    }

    public Target getUptarget() {
        return uptarget;
    }

    public void setUptarget(Target uptarget) {
        this.uptarget = uptarget;
    }

    public void initTarget(Target target){
        this.uptarget = target;
        if(StringUtils.isEmpty(this.id)){
            this.ctarget = target;
        }
    }

}
