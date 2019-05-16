package com.needto.pay.service;

import com.needto.common.entity.Dict;
import com.needto.pay.entity.CallbackData;
import com.needto.pay.entity.IPayData;

/**
 * @author Administrator
 */
public interface Deal<T extends IPayData> {

    /**
     * 发起支付请求
     * @return 返回付款链接
     */
    String prepare(T payData);

    /**
     * 支付结果回调
     * @return
     */
    void payCallback(CallbackData callback);

    String code();

    default String name(){
        return "";
    }

    default String desc() { return ""; }

}
