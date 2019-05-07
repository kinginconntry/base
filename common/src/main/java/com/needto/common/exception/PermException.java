package com.needto.common.exception;

/**
 * 权限异常
 * @author Administrator
 */
public class PermException extends BaseException {

    public PermException(Throwable e, String errCode, String errMsg) {
        super(e, errCode, errMsg);
    }

    public PermException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
