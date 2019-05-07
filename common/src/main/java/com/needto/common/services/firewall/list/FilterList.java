package com.needto.common.services.firewall.list;

import com.needto.common.dao.models.BaseEntity;
import com.needto.common.entity.Target;

import java.util.Date;

/**
 * @author Administrator
 * 黑白名单名单
 */
public class FilterList extends BaseEntity {

    public static final String TABLE = "_blacklist";

    public enum Mode {
        /**
         * 黑名单
         */
        BLACK,
        /**
         * 白名单
         */
        WHITE;
    }

    public enum Type{
        /**
         * 平台
         */
        PF,
        /**
         * ip阻止
         */
        IP,
        /**
         * 用户阻止
         */
        USER;

        public static boolean contain(String type){
            for(Type temp : Type.values()){
                if(temp.name().equals(type)){
                    return true;
                }
            }
            return false;
        }
    }
    /**
     * 拦截模式
     * @see Mode
     */
    protected String mode;

    /**
     * 目标
     * @see Type
     */
    protected Target target;

    /**
     * 阻止原因
     */
    protected String reason;

    /**
     * 阻止开始时间：必须有
     */
    protected Date start;

    /**
     * 阻止结束时间：若没有则一直存在名单拦截
     */
    protected Date end;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
