package com.needto.dao.models;

import com.needto.common.entity.Target;
import com.needto.dao.inter.ICtarget;
import com.needto.dao.inter.IUptarget;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 * 跟目标相关的类
 */
public class TargetEntity extends BaseEntity implements ICtarget, IUptarget {

    /**
     * 创建者
     */
    protected Target ctarget;

    /**
     * 更新者
     */
    protected Target uptarget;

    @Override
    public Target getCtarget() {
        return ctarget;
    }

    @Override
    public void setCtarget(Target ctarget) {
        this.ctarget = ctarget;
    }

    @Override
    public Target getUptarget() {
        return uptarget;
    }

    @Override
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
