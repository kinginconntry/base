package com.needto.simulate.entity;

import com.needto.http.entity.HttpHeader;
import com.needto.http.entity.Method;
import com.needto.tool.entity.Dict;
import com.needto.tool.entity.UrlInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * 请求体
 */
public class Request {

    /**
     * 请求的唯一id
     */
    protected String belong;

    /**
     * 请求url
     */
    protected UrlInfo urlInfo;

    /**
     * 请求方法
     */
    protected String method;

    /**
     * 请求头
     */
    protected HttpHeader headers;

    /**
     * 请求body
     */
    protected byte[] body;

    /**
     * 生成时间
     */
    protected long time;

    /**
     * 浏览器配置
     */
    protected BrowserConfig browserConfig;

    /**
     * 代理配置
     */
    protected ProxyData proxyData;

    /**
     * 配置
     */
    protected Dict config;

    public Request(String belong) {
        this.belong = belong;
    }

    public static Request get(String belong){
        Request request = new Request(belong);
        request.setMethod(Method.GET.key);
        return request;
    }

    public static Request post(String belong){
        Request request = new Request(belong);
        request.setMethod(Method.POST.key);
        return request;
    }

    public static Request delete(String belong){
        Request request = new Request(belong);
        request.setMethod(Method.DELETE.key);
        return request;
    }

    public static Request put(String belong){
        Request request = new Request(belong);
        request.setMethod(Method.PUT.key);
        return request;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpHeader getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeader headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public BrowserConfig getBrowserConfig() {
        return browserConfig;
    }

    public void setBrowserConfig(BrowserConfig browserConfig) {
        this.browserConfig = browserConfig;
    }

    public UrlInfo getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(UrlInfo urlInfo) {
        this.urlInfo = urlInfo;
    }

    public Dict getConfig() {
        return config;
    }

    public <T> T getConfig(String key) {
        if(StringUtils.isEmpty(key) || this.config == null){
            return null;
        }
        return config.getValue(key);
    }

    public void setConfig(Dict config) {
        this.config = config;
    }

    public Request addConfig(String key, Object o){
        if(StringUtils.isEmpty(key)){
            return this;
        }
        if(this.config == null){
            this.config = new Dict();
        }
        this.config.put(key, o);
        return this;
    }

    public ProxyData getProxyData() {
        return proxyData;
    }

    public void setProxyData(ProxyData proxyData) {
        this.proxyData = proxyData;
    }

    public boolean isUseBrowser(){
        return this.browserConfig != null;
    }
}
