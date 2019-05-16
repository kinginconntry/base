package com.needto.pay.entity;

import com.needto.common.entity.Dict;

public class CallbackData extends Dict {
    /**
     * 支付唯一;
     */
    public String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
