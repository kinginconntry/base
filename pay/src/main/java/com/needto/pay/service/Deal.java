package com.needto.pay.service;

import com.needto.common.entity.Dict;
import com.needto.common.inter.Thing;
import com.needto.pay.entity.IPayData;

/**
 * @author Administrator
 */
public interface Deal<T extends IPayData> extends Thing {

    /**
     * 发起支付请求
     * @return 返回付款链接
     */
    String prepare(T payData);

    /**
     * 支付结果回调
     * @return
     */
    void payCallback(Dict callback);

}
