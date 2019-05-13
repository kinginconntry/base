package com.needto.client;

public interface IDynamicClient {

    /**
     * 获取签名
     * @return
     */
    String getClientIde();
    boolean isValid(String sign);
}
