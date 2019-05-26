package com.needto.common.entity;

public class PageResult<T> {

    private boolean success;

    private T data;

    private String error;

    private String message;

    private long total;

    private int page;

    public static <T> PageResult<T> forSuccess(long total, int page, T data){
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setPage(page);
        pageResult.setData(data);
        pageResult.setSuccess(true);
        return pageResult;
    }

    public static <T> PageResult<T> forError(String message, String error){
        PageResult pageResult = new PageResult();
        pageResult.setSuccess(true);
        pageResult.setError(error);
        pageResult.setMessage(message);
        return pageResult;
    }

    public static <T> PageResult<T> forError(){
        return forError(null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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
}
