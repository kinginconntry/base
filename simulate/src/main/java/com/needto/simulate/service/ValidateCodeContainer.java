package com.needto.simulate.service;

import com.needto.common.service.ThingContainerService;
import com.needto.simulate.entity.IValidateCode;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * 验证码识别
 */
@Component
public class ValidateCodeContainer extends ThingContainerService<IValidateCode> {
    @Override
    protected Class<IValidateCode> getThingClass() {
        return IValidateCode.class;
    }
}
