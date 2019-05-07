package com.needto.common.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator
 * 客户端类型
 */
public enum ClientType {
    /**
     * web客户端
     */
    WEB,
    /**
     * token客户端
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
