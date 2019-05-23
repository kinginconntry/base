package com.needto.dao.models;

import com.alibaba.fastjson.annotation.JSONField;
import com.needto.common.inter.IInit;
import com.needto.dao.inter.ICtime;
import com.needto.dao.inter.IDelete;
import com.needto.dao.inter.IUptime;
import com.needto.dao.inter.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 * dao基础模型
 */
public class BaseEntity implements Serializable, IInit, Id, ICtime, IUptime, IDelete {

    /**
     * 系统默认的时间格式化信息
     */
    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * id
     */
    public static final transient String ID = "id";

    /**
     * id
     */
    protected String id;

    /**
     * 对id进行混淆后的id
     */
    protected String confuseId;

    /**
     * 创建时间
     */
    @JSONField(format = DATETIME_FORMAT)
    protected Date ctime;

    /**
     * 更新时间
     */
    @JSONField(format = DATETIME_FORMAT)
    protected Date uptime;

    /**
     * 删除标记
     */
    protected boolean deleted;

    public String getConfuseId() {
        return confuseId;
    }

    public void setConfuseId(String confuseId) {
        this.confuseId = confuseId;
    }

    @Override
    public Date getCtime() {
        return ctime;
    }

    @Override
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    @Override
    public Date getUptime() {
        return uptime;
    }

    @Override
    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
