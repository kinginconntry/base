package com.needto.common.entity;

import com.needto.common.exception.BaseException;
import com.needto.common.utils.ValidateUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
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

    /**
     * 根据完整的url获取url的协议，端口，域名，路径，参数等信息
     * @param url
     * @return
     */
    public static UrlInfo create(String url){
        if(!ValidateUtils.isUrl(url)){
            throw new BaseException("INVLID_URL", "");
        }
        UrlInfo urlInfo = new UrlInfo();
        String protocol = "";
        String host = "";
        String domain = "";
        String port = "80";
        String path = "";
        int index = url.indexOf("://");
        protocol = url.substring(0, index);
        String temp = url.substring(index + 3);

        int a = temp.indexOf("/");
        if(a > -1){
            host = temp.substring(0, a);
            path = temp.substring(a);
        }else{
            host = temp;
        }

        int m = host.indexOf(":");
        if(m > -1){
            domain = host.substring(0, m);
            port = host.substring(m + 1);
        }else{
            domain = host;
        }

        String params = "";
        int q = params.indexOf("?");
        if(q > -1){
            params = path.substring(q + 1);
            path = path.substring(0, q);
        }
        List<UrlInfo.UrlParam> urlParams = null;
        if(!StringUtils.isEmpty(params)){
            urlParams = new ArrayList<>();
            String[] paramList = params.split("&");
            for(int i = 0, len = paramList.length; i < len; i++){
                String param = paramList[i];
                if(StringUtils.isEmpty(param)){
                    continue;
                }
                String[] paramKv = param.split("=");
                if(paramKv.length == 1){
                    urlParams.add(new UrlInfo.UrlParam(paramKv[0], ""));
                }else if(paramKv.length == 2){
                    urlParams.add(new UrlInfo.UrlParam(paramKv[0], paramKv[1]));
                }else if(paramKv.length > 2){
                    int c = param.indexOf("=");
                    urlParams.add(new UrlInfo.UrlParam(param.substring(0, c), param.substring(c + 1)));
                }
            }
        }

        urlInfo.setUrl(url);
        urlInfo.setProtocol(protocol);
        urlInfo.setHost(host);
        urlInfo.setDomain(domain);
        urlInfo.setPort(port);
        urlInfo.setPath(path);
        urlInfo.setUrlParamList(urlParams);
        return urlInfo;
    }

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
