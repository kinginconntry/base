package com.needto.simulate.entity;

import com.needto.common.entity.Cookie;
import com.needto.tool.entity.Dict;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessResult {
    public Map<String, String> headers;
    public List<Cookie> cookies;

    /**
     * 上一步执行结果
     */
    public Object lastProcessRes;


    public Dict nextProcessData;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public void addCookies(List<Cookie> cookies){
        if(cookies == null){
            return;
        }
        if(this.cookies == null){
            this.cookies = new ArrayList<>();
        }
        this.cookies.addAll(cookies);
    }

    public void addHeaders(Map<String, String> headers){
        if(headers == null){
            return;
        }
        if(this.headers == null){
            this.headers = new HashMap<>();
        }
        this.headers.putAll(headers);
    }

    public Object getLastProcessRes() {
        return lastProcessRes;
    }

    public void setLastProcessRes(Object lastProcessRes) {
        this.lastProcessRes = lastProcessRes;
    }

    public Dict getNextProcessData() {
        return nextProcessData;
    }

    public void setNextProcessData(Dict nextProcessData) {
        this.nextProcessData = nextProcessData;
    }

    public void addNextProcessData(String key, Object data){
        if(StringUtils.isEmpty(key)){
            return;
        }
        if(this.nextProcessData == null){
            this.nextProcessData = new Dict();
        }
        this.nextProcessData.put(key, data);
    }
}
