package com.needto.thirdauth.data;

import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 * 第三方授权数据
 */
public class ThirdAuth {
    /**
     * 内部guid
     */
    private String guid;

    /**
     * 第三方guid
     */
    private String thirdGuid;

    /**
     * 第三方类型
     */
    private String type;

    /**
     * 第三方数据
     */
    private Dict data;

    public ThirdAuth(String guid, String thirdGuid, String type, Dict data) {
        this.guid = guid;
        this.thirdGuid = thirdGuid;
        this.type = type;
        this.data = data;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThirdGuid() {
        return thirdGuid;
    }

    public void setThirdGuid(String thirdGuid) {
        this.thirdGuid = thirdGuid;
    }

    public Dict getData() {
        return data;
    }

    public void setData(Dict data) {
        this.data = data;
    }
}
