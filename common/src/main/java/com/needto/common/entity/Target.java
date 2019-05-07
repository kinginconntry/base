package com.needto.common.entity;

/**
 * @author Administrator
 * 数据归属
 */
public class Target {

    /**
     * 应用所属平台操作日志
     */
    public static final String PF_GUID = "_PF:PF_";

    /**
     * 整个应用操作日志
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
        APP,
        /**
         * 系统管理内部用户
         */
        SYS,
        /**
         * 系统运营用户
         */
        ADMIN,
        /**
         * sso主用户
         */
        OWNER,
        /**
         * 账户
         */
        ACCOUNT,
        /**
         * 分组
         */
        GROUP,
        /**
         * 组织
         */
        ORG,
        /**
         * 角色
         */
        ROLE,
        /**
         * 某个用户
         */
        USER;

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
     * 归属于某个平台
     * @return
     */
    public static Target pf(){
        return new Target(Type.APP.name(), PF_GUID);
    }

    /**
     * 归属于某个应用
     * @return
     */
    public static Target app(){
        return new Target(Type.APP.name(), APP_GUID);
    }

    public static Target app(String guid){
        return new Target(Type.APP.name(), guid);
    }

    /**
     * 归属于某个系统用户
     * @param guid
     * @return
     */
    public static Target sys(String guid){
        return new Target(Type.SYS.name(), guid);
    }

    /**
     * 归属于某个运营用户
     * @param guid
     * @return
     */
    public static Target admin(String guid){
        return new Target(Type.ADMIN.name(), guid);
    }

    /**
     * 归属于某个分组
     * @param guid
     * @return
     */
    public static Target group(String guid){
        return new Target(Type.GROUP.name(), guid);
    }

    /**
     * 归属于某个组织
     * @param guid
     * @return
     */
    public static Target org(String guid){
        return new Target(Type.ORG.name(), guid);
    }

    /**
     * 归属于某个主用户
     * @param guid
     * @return
     */
    public static Target owner(String guid){
        return new Target(Type.OWNER.name(), guid);
    }

    /**
     * 归属于某个角色
     * @param guid
     * @return
     */
    public static Target role(String guid){
        return new Target(Type.ROLE.name(), guid);
    }

    /**
     * 归属于某个账户
     * @param guid
     * @return
     */
    public static Target account(String guid){
        return new Target(Type.ACCOUNT.name(), guid);
    }

    /**
     * 归属于某个用户
     * @param guid
     * @return
     */
    public static Target user(String guid){
        return new Target(Type.USER.name(), guid);
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
}
