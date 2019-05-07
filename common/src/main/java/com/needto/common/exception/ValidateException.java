package com.needto.common.exception;

/**
 * @author Administrator
 * 参数检查异常
 */
public class ValidateException extends BaseException {

    public ValidateException(String errMsg) {
        super("", errMsg);
    }

    public ValidateException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }

    public ValidateException(Throwable e, String errCode, String errMsg) {
        super(e, errCode, errMsg);
    }
}
