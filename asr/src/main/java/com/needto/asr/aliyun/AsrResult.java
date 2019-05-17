package com.needto.asr.aliyun;

import java.util.UUID;

/**
 * @author Administrator
 * @date 2018/5/31 0031
 * 封装的语音识别结果
 */
public class AsrResult<T> {

    public final static transient int DEFAULT_SUCCESS_STATUS = 200;

    /**
     * 语音结果内部唯一id，不被序列化
     */
    private transient UUID id;

    /**
     * 是否转换成功
     */
    private boolean success = true;

    /**
     * 状态码
     */
    private int status;

    /**
     * 返回说明
     */
    private String msg;

    /**
     * 语音数据
     */
    private T out;

    public AsrResult() {
    }

    public AsrResult(boolean success) {
        this.success = success;
    }

    public AsrResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public AsrResult(boolean success, int status, String msg, T out) {
        this.success = success;
        this.status = status;
        this.msg = msg;
        this.out = out;
    }

    public static <S> AsrResult<S> forSuccess() {
        return new AsrResult(true);
    }

    public static <S> AsrResult<S> forSuccess(String msg, S out) {
        return new AsrResult(true, DEFAULT_SUCCESS_STATUS, msg, out);
    }

    public static <S> AsrResult<S> forSuccess(int status, S out) {
        return new AsrResult(true, status, "", out);
    }

    public static <S> AsrResult<S> forSuccess(int status, String msg, S out) {
        return new AsrResult(true, status, msg, out);
    }

    public static <S> AsrResult<S> forError() {
        return new AsrResult(false);
    }

    public static <S> AsrResult<S> forError(String msg) {
        return new AsrResult(false, msg);
    }

    public static <S> AsrResult<S> forError(int status, String msg) {
        return new AsrResult(false, status, msg, null);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getOut() {
        return out;
    }

    public void setOut(T out) {
        this.out = out;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
