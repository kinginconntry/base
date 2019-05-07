package com.needto.common.dao.models;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 * dao基础模型
 */
public class BaseEntity implements Serializable {

    /**
     * 系统默认的时间格式化信息
     */
    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 创建者或更新者为系统时配置的数据
     */
    public static final transient String SYS_USER = "__SYS__";

    /**
     * 创建时间字段
     */
    public static final transient String CTIME = "ctime";

    /**
     * 更新时间字段
     */
    public static final transient String UPTIME = "uptime";

    /**
     * 创建者字段
     */
    public static final transient String CUSER = "cuser";

    /**
     * 更新者字段
     */
    public static final transient String UPUSER = "upuser";

    /**
     * id 被混淆之后的数据
     */
    public static final transient String CONFUSE_ID = "confuseId";

    /**
     * id
     */
    public static final transient String ID = "id";

    /**
     * 删除字段
     */
    public static final transient String DELETED = "deleted";

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
     * 创建者
     */
    protected String cuser;

    /**
     * 更新时间
     */
    @JSONField(format = DATETIME_FORMAT)
    protected Date uptime;

    /**
     * 更新者
     */
    protected String upuser;

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

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUptime() {
        return uptime;
    }

    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public String getUpuser() {
        return upuser;
    }

    public void setUpuser(String upuser) {
        this.upuser = upuser;
    }
}
