package com.needto.discount.service;

import com.needto.common.service.ThingContainerService;
import com.needto.discount.entity.DiscountMulti;
import com.needto.discount.entity.DiscountMultiResult;
import com.needto.discount.entity.DiscountUnit;
import com.needto.discount.entity.DiscountUnitResult;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Administrator
 */
public class DiscountService extends ThingContainerService<IDiscount> {

    @Override
    protected Class<IDiscount> getThingClass() {
        return IDiscount.class;
    }

    public DiscountMultiResult discount(DiscountMulti discountMulti){
        if(discountMulti == null){
            return null;
        }
        List<DiscountUnit> discountUnitList = discountMulti.getDiscountUnitList();
        DiscountMultiResult discountMultiResult = new DiscountMultiResult();
        if(CollectionUtils.isEmpty(discountUnitList)){
            return discountMultiResult;
        }

        for(DiscountUnit discountUnit : discountUnitList){
            IDiscount iDiscount = this.get(discountUnit.getCode());
            DiscountUnitResult discountResult = null;
            if(iDiscount != null){
                discountResult = iDiscount.cal(discountUnit);
            }
            if(discountResult == null){
                discountResult = new DiscountUnitResult();
                discountResult.setSuccess(false);
                discountResult.setError("INVALID");
            }
            discountResult.setCode(discountUnit.getCode());
            discountMultiResult.addUnitResult(discountResult);
        }
        return discountMultiResult;
    }
}
