package com.needto.simulate.entity;

/**
 * @author Administrator
 */
public class ValidateData {

    public String guid;
    /**
     * 验证类型
     */
    public String type;
    /**
     * 数据
     */
    public Object data;

    /**
     * 验证结果
     */
    public String codeRes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCodeRes() {
        return codeRes;
    }

    public void setCodeRes(String codeRes) {
        this.codeRes = codeRes;
    }
}
