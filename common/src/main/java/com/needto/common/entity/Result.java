package com.needto.common.entity;

import java.io.Serializable;

/**
 * 不同层之间返回结果
 * @author Administrator
 */
public class Result<T> implements Serializable {

    /**
     * 状态码，非序列化字段，用于错误情况下直接利用http状态返回，默认直接返回200
     */
    public transient Integer statusCode;

    public boolean success = true;
    private String error;
    private String message;
    public T data;

    public static <S> Result<S> forSuccessIfNotNull(S data) {
        if(data == null){
            return Result.forError("", "");
        }else{
            return new Result<S>(true, null, null, data);
        }
    }

    public static <S> Result<S> forSuccess() {
        return new Result<S>(true, null, null, null);
    }

    public static <S> Result<S> forSuccess(S data) {
        return new Result<S>(true, null, null, data);
    }

    public static <S> Result<S> forError(String error, String message, S data) {
        return new Result<S>(false, error, message, data);
    }

    public static <S> Result<S> forError(String error, String message) {
        return new Result<S>(false, error, message, null);
    }

    public static <S> Result<S> forError() {
        return new Result<S>(false, null, null, null);
    }

    /**
     * 重定向消息
     * @param redirect 重定向地址
     * @param data 重定向额外数据
     * @param <S>
     * @return
     */
    public static <S> Result<Redirect> forRedirect(String redirect, S data) {
        Redirect redirectData = new Redirect();
        redirectData.setData(data);
        redirectData.setRedirect(redirect);
        return new Result<>(false, String.format("%s", "404"), "", redirectData);
    }

    public Result(){}

    public Result(boolean success){
        this.success = success;
    }

    public Result(boolean success, String error, String message, T data) {
        this.success = success;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
