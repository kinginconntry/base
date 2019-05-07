package com.needto.services.notice;

import com.needto.common.entity.Target;
import com.needto.dao.models.BaseEntity;

import java.util.Date;

/**
 * @author Administrator
 * 站内消息
 */
public class Notice extends BaseEntity {

    public final static String TABLE = "_notice";

    public final static String SYS_SOURCE_TYPE = "_SYS_TYPE";

    public final static String SYS_SOURCE = "_SYS";

    public enum Type{
        /**
         * 系统公告，所有人都能接受
         */
        BROADCAST,
        /**
         * 目标对目标的通知
         */
        OTHER;

    }

    /**
     * 类型
     * @see Type
     */
    private String type;

    /**
     * （来源）发送者
     */
    private Target source;

    /**
     * （目标）接受者
     */
    private Target target;

    /**
     * 消息实体
     */
    private NoticeMsg data;

    /**
     * 开始时间，这个时间点之后的所有目标都能接受到消息
     */
    private Date startTime;

    /**
     * 消息结束时间
     * 定期删除
     */
    private Date endTime;

    /**
     * 对象对比当前的版本拉取站内消息（拉取所有高于目标当前版本的消息）
     */
    private long version;

    public Notice() {
        this.startTime = new Date();
        this.version = System.currentTimeMillis();
        this.target = new Target();
        this.source = new Target();
    }

    public void initVersion(){
        this.version = System.currentTimeMillis();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Target getSource() {
        return source;
    }

    public void setSource(Target source) {
        this.source = source;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public NoticeMsg getData() {
        return data;
    }

    public void setData(NoticeMsg data) {
        this.data = data;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
