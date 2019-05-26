package com.needto.web.data;

public enum ClientType {

    /**
     * 未授权
     */
    NO_AUTH,
    /**
     * 基于token授权
     */
    TOKEN;

    public static boolean contain(String type){
        for(ClientType clientType : ClientType.values()){
            if(clientType.name().equals(type)){
                return true;
            }
        }
        return false;
    }
}
