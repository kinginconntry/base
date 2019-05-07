package com.needto.common.entity;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 分页查询数据
 *
 * @author Administrator
 */
public class PageResult<T> implements Serializable {

    /**
     * 状态码，非序列化字段，用于错误情况下直接利用http状态返回，默认直接返回200
     */
    public transient Integer statusCode;

    public boolean success = true;
    private String error;
    private String message;
    public long total;
    public int page;
    public T data;

    public PageResult(){}

    public PageResult(boolean success, String error, String message, T data) {
        this.success = success;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public PageResult(long total, int page, T data) {
        this.total = total;
        this.page = page;
        this.data = data;
    }

    public static <T> PageResult<T> forSuccess(long total, int page, T data) {
        return new PageResult<>(total, page, data);
    }

    public static <T> PageResult<T> forError(String error, String message) {
        return new PageResult<>(false, error, message, null);
    }

    public static <T> PageResult<T> forError(String error, String message, T data) {
        return new PageResult<>(false, error, message, data);
    }

    /**
     * 重定向结果
     * @param redirect 重定向地址
     * @param data 重定向额外数据
     * @param <T>
     * @return
     */
    public static <T> PageResult<Redirect> forRedirect(String redirect, T data) {
        Redirect redirectData = new Redirect();
        redirectData.setData(data);
        redirectData.setRedirect(redirect);
        return new PageResult<>(false, String.format("%s", HttpServletResponse.SC_FOUND), "", redirectData);
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
