package com.needto.thirdauth.data;


/**
 * @author Administrator
 * 第三方授权相关内部guid
 */
public class GuidData {

    /**
     * 本地数据唯一id
     */
    private String localId;

    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误消息
     */
    private String msg;

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
