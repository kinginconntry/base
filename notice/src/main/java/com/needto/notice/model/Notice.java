package com.needto.notice.model;

import com.needto.common.entity.Target;
import com.needto.dao.models.UserEntity;
import com.needto.notice.entity.AbstractNoticeMsg;

import java.util.Date;

/**
 * @author Administrator
 * 系统消息
 */
public class Notice extends UserEntity {

    public final static String TABLE = "_notice";

    /**
     * （目标）接受者
     */
    private Target target;

    /**
     * 消息实体
     */
    private AbstractNoticeMsg data;

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

    public AbstractNoticeMsg getData() {
        return data;
    }

    public void setData(AbstractNoticeMsg data) {
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
}
