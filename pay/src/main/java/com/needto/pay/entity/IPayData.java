package com.needto.pay.entity;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
public interface IPayData {

    /**
     * 支付唯一id
     * @return
     */
    String guid();

    /**
     * 支付金额
     * @return
     */
    BigDecimal ammount();
}
