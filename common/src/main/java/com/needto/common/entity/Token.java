package com.needto.common.entity;

import com.needto.tool.utils.Utils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 资源调用token
 * @author Administrator
 */
public class Token<T>{

    /**
     * token值
     */
    protected String token;

    /**
     * token刷新时需要的值
     */
    protected String refreshToken;

    /**
     * 使用范围
     */
    protected String scope;

    /**
     * 类型对应的全局id
     */
    protected String guid;

    /**
     * token类型
     */
    protected String type;

    /**
     * 已调用次数
     */
    protected int used;

    /**
     * 可使用总次数
     */
    protected Integer total;

    /**
     * 有效期：开始时间
     */
    protected Date stime = null;

    /**
     * 有效期：结束时间
     */
    protected Date etime = null;

    /**
     * 创建的来源
     */
    protected Target source;

    /**
     * 使用的app
     */
    protected Set<String> apps = null;

    protected T data;

    public Token() {
    }

    public Token(String scope, String guid) {
        this.scope = scope;
        this.guid = guid;
    }

    public void init(){
        this.token = Utils.getGuidBase64();
        this.refreshToken = Utils.getGuidBase64();
        this.apps = new HashSet<>();
        if(this.stime == null){
            this.stime = new Date();
        }
    }

    public void outFormat(){
        this.apps = null;
    }

    /**
     * 设置有效期
     */
    public void initExpire(Date stime, Date etime) {
        if (stime == null) {
            stime = new Date();
        }
        this.stime = stime;
        this.etime = etime;
    }

    /**
     * @param stime
     * @param expire 过期时间：秒
     */
    public void initExpire(Date stime, Long expire) {
        if (stime == null) {
            stime = new Date();
        }
        this.stime = stime;
        if (expire != null) {
            this.etime = new Date(this.stime.getTime() + expire * 1000);
        }
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Date getStime() {
        return stime;
    }

    public void setStime(Date stime) {
        this.stime = stime;
    }

    public Date getEtime() {
        return etime;
    }

    public void setEtime(Date etime) {
        this.etime = etime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 只检查是否超过调用次数
     * 在redis存储时，这里会被序列化成字段
     * @return
     */
    public boolean checkLimited(){
        if(this.total == null){
            return false;
        }

        return this.used >= this.total;
    }

    /**
     * 是否过期
     * @return
     */
    public boolean checkExpired(){
        if(this.etime == null){
           return false;
        }

        return System.currentTimeMillis() >= this.etime.getTime();
    }

    /**
     * token是否可用
     * @return
     */
    public boolean checkAvaiabled(){
        return !this.checkExpired() && !this.checkLimited();
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * 毫秒
     * @return
     */
    public Long calExpire(){
        if(this.stime == null || this.etime == null){
            return null;
        }
        return this.etime.getTime() - this.stime.getTime();
    }

    /**
     * @param expire 毫秒
     */
    public void initExpire(long expire){
        if(expire <= 0){
            expire = 1;
        }
        if(this.stime == null){
            this.stime = new Date();
        }
        this.etime = new Date(this.stime.getTime() + expire);
    }

    public void refreshToken(long expire){
        this.initExpire(expire);
        this.refreshToken = getGuid();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Set<String> getApps() {
        return apps;
    }

    public void setApps(Set<String> apps) {
        this.apps = apps;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Target getSource() {
        return source;
    }

    public void setSource(Target source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
