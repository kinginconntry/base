package com.needto.dao.inter;

import com.needto.common.entity.Target;

public interface ICtarget {
    /**
     * 创建者字段
     */
    String CTARGET = "ctarget";

    Target getCtarget();

    void setCtarget(Target ctarget);
}
