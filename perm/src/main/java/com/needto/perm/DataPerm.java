package com.needto.perm;

import com.needto.common.entity.Target;
import com.needto.dao.models.UserEntity;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 数据权限控制实体
 * @author Administrator
 */
public class DataPerm extends UserEntity {

    public static final String TABLE = "_dataperm";

    public interface Condition {
        enum Type{
            /**
             * 单条件
             */
            SINGLE,
            /**
             * 多条件
             */
            MULTI;
        }

        /**
         * 条件类型
         * @return
         */
        String type();
    }

    /**
     * 单条件菜单
     */
    public static class SingleCondition implements Condition {
        /**
         * 目标
         */
        public Target target;
        /**
         * 目标与值关系
         */
        public String relate;
        /**
         * 值
         */
        public Object value;

        public Target getTarget() {
            return target;
        }

        public void setTarget(Target target) {
            this.target = target;
        }

        public String getRelate() {
            return relate;
        }

        public void setRelate(String relate) {
            this.relate = relate;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String type() {
            return Type.SINGLE.name();
        }
    }

    /**
     * 多条件菜单
     */
    public static class MultiCondition implements Condition {

        /**
         * 多条件关系
         */
        public enum Relation {
            /**
             * 全匹配
             */
            MUST,
            /**
             * 全都不匹配
             */
            NOT,
            /**
             * 至少有一个匹配
             */
            SHOULD
        }

        /**
         * 子条件之间的关系
         */
        public String relate;

        /**
         * 子条件集合
         */
        public List<Condition> childrens;

        public String getRelate() {
            return relate;
        }

        public void setRelate(String relate) {
            this.relate = relate;
        }

        public List<Condition> getChildrens() {
            return childrens;
        }

        public void setChildrens(List<Condition> childrens) {
            this.childrens = childrens;
        }

        @Override
        public String type() {
            return Type.MULTI.name();
        }
    }

    public enum Op {
        /**
         * 增加
         */
        ADD("0"),
        /**
         * 查看
         */
        LOOK("1"),
        /**
         * 更新
         */
        UPDATE("2"),
        /**
         * 删除
         */
        DELETE("3");
        public String key;
        Op(String key){
            this.key = key;
        }

        public static boolean contain(String key){
            if(StringUtils.isEmpty(key)){
                return false;
            }
            for(Op op : Op.values()){
                if(op.key.equals(key)){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 主用户id
     */
    protected String owner;

    /**
     * 数据源（集合，表，其他数据）
     */
    protected String source;

    /**
     * 数据源目标
     */
    protected Target target;

    /**
     * 执行操作
     */
    protected Op op;

    /**
     * 操作是否允许
     */
    protected Boolean operable;

    /**
     * 操作执行条件
     */
    protected Condition condition;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOp() {
        if(this.op == null){
            return null;
        }else{
            return this.op.key;
        }
    }

    public void setOp(String op) {
        this.op = Op.valueOf(op);
    }

    public Boolean getOperable() {
        return operable;
    }

    public void setOperable(Boolean operable) {
        this.operable = operable;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
