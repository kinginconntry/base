package com.needto.notice.model;

import com.needto.common.entity.Target;
import com.needto.dao.models.TargetEntity;
import com.needto.notice.entity.NoticeMsg;

import java.util.Date;

/**
 * @author Administrator
 * 系统消息(可能是管理人员或者系统自动发布)
 */
public class Notice extends TargetEntity {

    public final static String TABLE = "_notice";

    /**
     * （目标）接受者
     */
    private Target target;

    /**
     * 消息实体
     */
    private NoticeMsg data;

    /**
     * 开始时间，这个时间点之后的目标能接受到消息
     */
    private Date startTime;

    /**
     * 结束时间，这个时间点后的目标无法接受消息
     * 定期删除
     */
    private Date endTime;

    /**
     * 强行提示
     */
    private boolean prompt;

    /**
     * 确认收到
     */
    private boolean ack;

    public Notice() {
        this.startTime = new Date();
        this.target = new Target();
        this.prompt = false;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public boolean isPrompt() {
        return prompt;
    }

    public void setPrompt(boolean prompt) {
        this.prompt = prompt;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }
}
