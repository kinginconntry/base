package com.needto.common.entity;

import java.io.Serializable;

/**
 * @author Administrator
 * 重定向数据
 */
public class Redirect implements Serializable {

    private static final long serialVersionUID = -5809782578272943999L;

    public String redirect;

    public Object data;

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
