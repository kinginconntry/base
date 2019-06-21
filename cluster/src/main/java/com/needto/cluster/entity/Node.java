package com.needto.cluster.entity;

/**
 * @author Administrator
 * 分布式节点
 */
public class Node<T> {

    /**
     * 分组
     */
    public String group;

    /**
     * 应用域名
     */
    public String domain;

    /**
     * 应用端口
     */
    public String port;

    /**
     * 应用名
     */
    public String appName;

    /**
     * 数据
     */
    public T data;

    /**
     * 节点版本
     */
    public String version;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
