package com.needto.common.entity;

import java.util.List;

public class UrlInfo {

    /**
     * url参数
     */
    public static class UrlParam {
        /**
         * 参数名
         */
        public String name;
        /**
         * 参数值
         */
        public String value;

        public UrlParam(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    /**
     * 完整url
     */
    public String url;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 主机
     */
    private String host;

    /**
     * 域名
     */
    private String domain;

    /**
     * 端口
     */
    private String port;

    /**
     * 请求路径，不带参数
     */
    private String path;

    /**
     * url参数
     */
    private List<UrlParam> urlParamList;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<UrlParam> getUrlParamList() {
        return urlParamList;
    }

    public void setUrlParamList(List<UrlParam> urlParamList) {
        this.urlParamList = urlParamList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
