package com.needto.common.webconfig;

import com.needto.common.context.GlobalEnv;
import com.needto.tool.entity.Result;
import com.needto.tool.exception.BaseException;
import com.needto.tool.exception.LogicException;
import com.needto.tool.utils.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * 全局拦截异常
 */
@RestController
@RestControllerAdvice
public class CusErrorController implements ErrorController {

    private static final Logger LOG = LoggerFactory.getLogger(CusErrorController.class);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public Object error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer status = (Integer)request.getAttribute("javax.servlet.error.status_code");
        return Result.forError(status + "", "");
    }

    /**
     * 通用异常拦截
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result<Void> baseException(HttpServletRequest request, HttpServletResponse response, LogicException e) throws IOException {
        if(ValidateUtils.containChinese(e.getErrMsg())){
            return Result.forError(e.getErrCode(), e.getMessage());
        }else{
            if(GlobalEnv.isDebug()){
                return Result.forError(e.getErrCode(), e.getMessage());
            }else{
                // 非中文的消息不想前台发送
                return Result.forError(e.getErrCode(), "");
            }
        }
    }
}
