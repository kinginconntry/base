package com.needto.client;

public interface DynamicClient {

    /**
     * 获取客户端标识
     * @return
     */
    String getClientIde();

    /**
     * 检查客户端标识是否有效
     * @param sign
     * @return
     */
    boolean isValid(String sign);
}
