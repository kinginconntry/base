package com.needto.perm;

import com.needto.perm.data.Constant;
import com.needto.perm.data.PermAuth;
import com.needto.tool.entity.Dict;
import com.needto.tool.entity.Result;
import com.needto.web.context.WebEnv;
import com.needto.web.utils.ResponseUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 功能权限拦截器
 */
@Component
public class PermissionIntercept implements HandlerInterceptor {

    private static final Dict DEFAULT_RESOURCE_PERMS = new Dict();

    private static final List<String> DEFAULT_FUNC_PERMS = new ArrayList<>(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        PermAuth validate = ((HandlerMethod) handler).getMethodAnnotation(PermAuth.class);
        if(validate != null){
            List<String> funcPerms = WebEnv.getClientCache().getValue(Constant.RESOURCE_KEY, DEFAULT_RESOURCE_PERMS).getValue(Constant.FUNCPERM_KEY, DEFAULT_FUNC_PERMS);
            if(CollectionUtils.isEmpty(funcPerms) || !funcPerms.contains(validate.value())){
                ResponseUtil.outJson(response, Result.forError(validate.errcode(), validate.msg()));
                return false;
            }
        }
        return true;
    }
}
