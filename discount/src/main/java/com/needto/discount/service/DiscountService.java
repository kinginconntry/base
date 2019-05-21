package com.needto.discount.service;

import com.needto.common.service.ThingContainerService;
import org.springframework.stereotype.Component;

@Component
public class DiscountService extends ThingContainerService<IDiscount> {

    @Override
    protected Class getThingClass() {
        return IDiscount.class;
    }
}
