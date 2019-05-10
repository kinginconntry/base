package com.needto.organization;

import com.needto.common.entity.Link;
import com.needto.dao.models.UserEntity;

import java.util.Map;

/**
 * 组织实体
 * @author Administrator
 */
public class Organition extends UserEntity {

    public static final String TABLE = "_organition";

    /**
     * 主用户id
     */
    private String owner;

    /**
     * 组织名
     */
    public String name;

    /**
     * 代表编码
     */
    public String code;

    /**
     * 描述
     */
    public String desc;

    /**
     * 简称
     */
    public String sname;
    /**
     * logo
     */
    public String logo;

    /**
     * 联系方式
     */
    public Map<String, String> contactMap;

    /**
     * 相关链接
     */
    public Map<String, Link> linkMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Map<String, Link> getLinkMap() {
        return linkMap;
    }

    public void setLinkMap(Map<String, Link> linkMap) {
        this.linkMap = linkMap;
    }

    public Map<String, String> getContactMap() {
        return contactMap;
    }

    public void setContactMap(Map<String, String> contactMap) {
        this.contactMap = contactMap;
    }
}
