package com.needto.common.entity;

import com.needto.common.utils.Utils;

/**
 * @author Administrator
 * 数据归属
 */
public class Target {

    /**
     * 应用所属平台
     */
    public static final String PF_GUID = "_PF:PF_";

    /**
     * 整个应用
     */
    public static final String APP_GUID = "_APP:APP_";

    /**
     * 默认提供的一些归属情况，其他需要自定义
     */
    public enum Type{
        /**
         * 整个平台
         */
        PF,
        /**
         * 应用
         */
        APP;

        public static boolean contain(String key) {
            for(Type type : Type.values()){
                if(type.name().equals(key)){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 归属类型
     * @see Type
     */
    public String type;

    /**
     * 归属目标id
     */
    public String guid;

    public Target() {
    }

    /**
     * 归属于某个平台：默认
     * @return
     */
    public static Target pf(){
        return new Target(Type.APP.name(), PF_GUID);
    }

    /**
     * 归属于某个平台：某个
     * @return
     */
    public static Target pf(String guid){
        return new Target(Type.APP.name(), guid);
    }

    /**
     * 归属于某个应用：默认
     * @return
     */
    public static Target app(){
        return new Target(Type.APP.name(), APP_GUID);
    }

    /**
     * 归属于某个应用：某个
     * @return
     */
    public static Target app(String guid){
        return new Target(Type.APP.name(), guid);
    }

    /**
     * 是否归属于应用
     * @param target
     * @return
     */
    public static boolean isApp(Target target){
        if(target != null && Type.APP.name().equals(target.type) && APP_GUID.equals(target.getGuid())){
            return true;
        }
        return false;
    }

    /**
     * 是否归属于平台
     * @param target
     * @return
     */
    public static boolean isPf(Target target){
        if(target != null && Type.PF.name().equals(target.type) && PF_GUID.equals(target.getGuid())){
            return true;
        }
        return false;
    }

    /**
     * 是否归属于机器人（应用或平台）
     * @param target
     * @return
     */
    public static boolean isRobot(Target target){
        return isPf(target) || isApp(target);
    }

    public Target(String type, String guid) {
        this.type = type;
        this.guid = guid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * 获取该对象的组合唯一id
     * @return
     */
    public String getTargetId(){
        return Utils.nullToString(this.type) + ":" + Utils.nullToString(this.guid);
    }
}
