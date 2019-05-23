package com.needto.dao.inter;

public interface ConfuseId {

    /**
     * id 被混淆之后的数据
     */
    String CONFUSE_ID = "confuseId";

    String getConfuseId();

    void setConfuseId(String confuseId);
}
