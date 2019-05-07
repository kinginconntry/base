package com.needto.common.context;

import com.needto.common.entity.Target;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */
@Component
public class DefaultClientInit implements IClientInit {

    @Override
    public void init(Target target, HttpServletRequest httpServletRequest) {

    }
}
