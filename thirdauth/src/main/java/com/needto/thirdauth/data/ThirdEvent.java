package com.needto.thirdauth.data;

/**
 * @author Administrator
 * 第三方事件
 */
public class ThirdEvent{

    /**
     * 第三方类型
     */
    private String type;

    /**
     * 事件数据
     */
    private Dict data;

    public ThirdEvent(String type, Dict data) {
        this.type = type;
        this.data = data;
    }

    public Dict getData() {
        return data;
    }

    public void setData(Dict data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
