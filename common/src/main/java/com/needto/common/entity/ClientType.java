package com.needto.common.entity;


import org.springframework.util.StringUtils;

/**
 * @author Administrator
 * 客户端类型
 */
public enum ClientType {
    /**
     * 非授权客户端（访客），通常指没有进行登录授权的用户
     */
    NO_AUTH,
    /**
     * token客户端，通过token进行授权的客户端
     */
    TOKEN;
    public static boolean contain(String key){
        if(StringUtils.isEmpty(key)){
            return false;
        }
        for(ClientType clientType : ClientType.values()){
            if(clientType.name().equals(key)){
                return true;
            }
        }
        return false;
    }
}
