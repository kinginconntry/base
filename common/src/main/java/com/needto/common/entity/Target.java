package com.needto.common.entity;

import com.needto.tool.entity.BaseTarget;
import com.needto.tool.utils.Utils;

/**
 * @author Administrator
 * 数据归属
 */
public class Target extends BaseTarget {

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

    public Target(){
        super();
    }

    public Target(String type, String name){
        super(type, name);
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

    public String getTargetId(){
        return Utils.nullToString(this.type) + "_" + Utils.nullToString(this.guid);
    }

}
