package com.needto.dao.inter;

import java.util.Date;

public interface ICtime {

    /**
     * 创建时间字段
     */
    String CTIME = "ctime";

    Date getCtime();

    void setCtime(Date ctime);
}
