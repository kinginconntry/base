package com.needto.common.entity;


/**
 * @author Administrator
 * 重定向统一返回数据
 */
public class Redirect{

    public String redirect;

    public Object data;

    public Redirect(String redirect) {
        this.redirect = redirect;
    }

    public Redirect(String redirect, Object data) {
        this.redirect = redirect;
        this.data = data;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
