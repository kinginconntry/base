package com.needto.discount.service;

import com.needto.common.inter.Thing;

import java.math.BigDecimal;

/**
 * @author Administrator
 * 打折接口
 */
public interface IDiscount extends Thing {

    /**
     *
     * @param key 折扣编号
     * @param fee 花费
     * @param auth 校验码
     * @return 若打折成功，返回折扣后的金额，否则抛出异常会
     */
    BigDecimal cal(String key, BigDecimal fee, String auth);

}
