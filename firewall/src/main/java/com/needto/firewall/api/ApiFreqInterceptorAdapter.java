package com.needto.firewall.api;

import com.needto.common.context.GlobalEnv;
import com.needto.common.entity.Result;
import com.needto.common.entity.Target;
import com.needto.common.utils.RequestUtil;
import com.needto.common.utils.ResponseUtil;
import com.needto.firewall.frequency.FrequencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * api频率拦截器
 */
@Service
public class ApiFreqInterceptorAdapter extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(ApiFreqInterceptorAdapter.class);

    @Autowired
    private FrequencyService frequencyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            ApiFreqLimit apiFreqLimit = ((HandlerMethod) handler).getMethodAnnotation(ApiFreqLimit.class);
            if(apiFreqLimit != null){
                int max = apiFreqLimit.max();
                int second = apiFreqLimit.second();
                int forbidtime = apiFreqLimit.forbidtime();
                if(max <= 0 || second <= 0 || forbidtime <= 0){
                    return super.preHandle(request, response, handler);
                }

                Target target = GlobalEnv.getClient(request);
                if(target == null && !apiFreqLimit.useIp()){
                    return super.preHandle(request, response, handler);
                }
                String source = ((HandlerMethod) handler).getBean().getClass().getName() + "_" + ((HandlerMethod) handler).getMethod().getName();
                String targetGuid;
                if(target != null){
                    targetGuid = target.getType() + "_" + target.getGuid();
                }else{
                    String ip = RequestUtil.getIp(request);
                    targetGuid = "IP_" + ip;
                    // ip拦截放大倍数
                    int times = apiFreqLimit.times();
                    if(times <= 0){
                        times = 1;
                    }
                    max = max * times;
                }
                if(frequencyService.filter(source, targetGuid, max, second, forbidtime)){
                    // 禁用
                    LOG.debug("api 访问频率过高, url {}, source {}, target {}, max {}, second {}", request.getRequestURI(), source, targetGuid, max, second);
                    ResponseUtil.outJson(response, Result.forError("403", ""));
                    return false;
                }
            }
        }
        return super.preHandle(request, response, handler);
    }
}