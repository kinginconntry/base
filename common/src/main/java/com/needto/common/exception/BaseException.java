package com.needto.common.exception;

/**
 * 基础异常类
 * @author zhangliang
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 8251368840238263405L;

    public final static String ERRCODE_DEFAULT_INNER = "500";

    /** @Fields errCode: 内部定义错误码 */
    private String errCode;

    /** @Fields errMsg: 错误信息 */
    private String errMsg;

    public BaseException(Throwable e, String errCode, String errMsg){
        super(e);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BaseException(String errCode,String errMsg){
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public static String getErrcodeDefaultInner() {
        return ERRCODE_DEFAULT_INNER;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
