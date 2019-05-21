package com.needto.chatpush.businessemail;

import com.needto.common.service.ThingContainerService;
import com.needto.common.utils.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class BusinessEmailContainer extends ThingContainerService<IEmailService> {

    @Override
    protected Class<IEmailService> getThingClass() {
        return IEmailService.class;
    }
}
