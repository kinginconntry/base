package com.needto.log.entity;

import com.needto.common.entity.Target;
import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 * 业务操作日志
 */
public class OpLog extends Log {

    public static final String TABLE = "_oplog";

    /**
     * 基本操作枚举，可扩展
     */
    public enum Type {
        /**
         * 登录
         */
        LOGIN,
        /**
         * 数据增加
         */
        ADD,
        /**
         * 数据修改
         */
        UPDATE,
        /**
         * 数据删除
         */
        DEL;
    }

    /**
     * 操作目标
     */
    public Target target;
    /**
     * 操作相关的数据
     */
    public Dict data;

    /**
     * 创建人
     */
    public String cuser;

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Dict getData() {
        return data;
    }

    public void setData(Dict data) {
        this.data = data;
    }

    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }
}
