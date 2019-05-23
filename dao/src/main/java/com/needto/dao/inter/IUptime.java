package com.needto.dao.inter;

import java.util.Date;

public interface IUptime {

    /**
     * 更新时间字段
     */
    String UPTIME = "uptime";

    Date getUptime();

    void setUptime(Date uptime);
}
