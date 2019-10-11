package com.needto.web.config;

import com.needto.common.context.SpringEnv;
import com.needto.tool.entity.Result;
import com.needto.tool.exception.BaseException;
import com.needto.tool.utils.ValidateUtils;
import com.needto.web.utils.RequestUtil;
import com.needto.web.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * 全局拦截异常
 */
@RestControllerAdvice
public class GlobalErrorController{

    private static final Logger LOG = LoggerFactory.getLogger(GlobalErrorController.class);

    @Autowired
    private ApplicationContext applicationContext;

    private ErrorPageProducer errorPageProducer;

    private ErrorPageProducer getErrorPageProducer(){
        if(errorPageProducer == null){
            synchronized (this){
                if(errorPageProducer == null){
                    try {
                        errorPageProducer = applicationContext.getBean(ErrorPageProducer.class);
                    }catch (Exception e){
                        errorPageProducer = new ErrorPageProducer() {};
                    }
                }
            }

        }
        return errorPageProducer;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {


        Integer status = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if(RequestUtil.isAjax(request)){

            if(e instanceof BaseException){
                BaseException err = (BaseException)e;
                if(ValidateUtils.containChinese(err.getErrMsg()) || SpringEnv.isDebug()){
                    // 包含中文信息
                    ResponseUtil.outJson(response, Result.forError(err.getErrCode(), e.getMessage()));
                }else{
                    // 非中文的消息不想前台发送
                    ResponseUtil.outJson(response, Result.forError(err.getErrCode(), ""));
                }

            }else{
                ResponseUtil.outJson(response, Result.forError(status + "", ""));
            }
            return null;
        }else{

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("EXCEPTION", e);
            modelAndView.setViewName(errorPageProducer.path(status, request, response, e));
            return modelAndView;
        }
    }
}
