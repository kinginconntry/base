package com.needto.discount.service;

import com.needto.discount.entity.DiscountUnit;
import com.needto.discount.entity.DiscountUnitResult;
import com.needto.tool.inter.Thing;

/**
 * @author Administrator
 * 打折接口
 */
public interface IDiscount extends Thing {

    /**
     * @return 若打折成功，返回折扣后的金额，否则抛出异常会
     */
    DiscountUnitResult cal(DiscountUnit discountUnit);

}
