package com.needto.common.exception;

/**
 * 逻辑异常，由应用程序自己判断抛出
 * @author Administrator
 */
public class LogicException extends BaseException {

    public final static String ERRCODE_DEFAULT_1001 = "1001";

    public LogicException(Throwable e, String errCode, String errMsg) {
        super(e, errCode, errMsg);
    }

    public LogicException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
