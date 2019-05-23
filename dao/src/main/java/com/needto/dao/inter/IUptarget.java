package com.needto.dao.inter;

import com.needto.common.entity.Target;

public interface IUptarget {

    /**
     * 更新者字段
     */
    String UPTARGET = "uptarget";

    Target getUptarget();

    void setUptarget(Target uptarget);
}
