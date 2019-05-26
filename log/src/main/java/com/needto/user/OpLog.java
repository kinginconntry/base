package com.needto.user;

import com.needto.common.entity.Target;
import com.needto.dao.models.UserEntity;
import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 * 操作日志
 */
public class OpLog extends UserEntity {

    public static final String TABLE = "_oplog";

    /**
     * 基本操作枚举，可扩展
     */
    public enum Op {
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
     * 日志归属
     */
    public String owner;
    /**
     * 日志操作
     * @see Op
     */
    public String op;
    /**
     * 操作名称
     */
    public String name;
    /**
     * 操作描述
     */
    public String desc;
    /**
     * 操作目标
     */
    public Target target;
    /**
     * 操作相关的数据
     */
    public Dict data;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

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
}
