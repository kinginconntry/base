package com.needto.simulate.entity;

import com.needto.http.entity.Cookie;
import com.needto.http.entity.HttpHeader;
import com.needto.tool.entity.Dict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 响应体
 */
public class Response {

    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 失败
     */
    public static final int FAILTURE = -1;

    /**
     * 请求体
     */
    protected Request request;

    /**
     * 响应header
     */
    protected HttpHeader headers;

    /**
     *
     */
    protected List<Cookie> cookies;

    /**
     * 响应码
     */
    protected int statuscode;

    /**
     * 失败原因
     */
    protected String error;

    /**
     * 编码
     */
    protected String charset;

    /**
     * 响应体
     */
    protected String body;

    /**
     * 请求开始时间
     */
    protected long requestStartTime;

    /**
     * 请求结束时间
     */
    protected long requestEndTime;

    /**
     * 配置
     */
    private Dict config;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public HttpHeader getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeader headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public String getCharset() {
        if(StringUtils.isEmpty(charset)){
            return DEFAULT_CHARSET;
        }
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Dict getConfig() {
        return config;
    }

    public void setConfig(Dict config) {
        this.config = config;
    }

    public long getRequestStartTime() {
        return requestStartTime;
    }

    public void setRequestStartTime(long requestStartTime) {
        this.requestStartTime = requestStartTime;
    }

    public long getRequestEndTime() {
        return requestEndTime;
    }

    public void setRequestEndTime(long requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public void addHeader(String key, String value){
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(value)){
            return;
        }
        if(this.headers == null){
            this.headers = new HttpHeader();
        }
        this.headers.setFirstHeader(key, value);
    }

    public void addHeader(Map<String, String> map){
        if(CollectionUtils.isEmpty(map)){
            return;
        }
        if(this.headers == null){
            this.headers = new HttpHeader();
        }
        this.headers.setHeaders(map);
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public void addCookie(Cookie cookie){
        if(cookie == null){
            return;
        }
        if(this.cookies == null){
            this.cookies = new ArrayList<>();
        }
        this.cookies.add(cookie);
    }

    public void addCookie(List<Cookie> cookies){
        if(cookies == null){
            return;
        }
        if(this.cookies == null){
            this.cookies = new ArrayList<>();
        }
        this.cookies.addAll(cookies);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
