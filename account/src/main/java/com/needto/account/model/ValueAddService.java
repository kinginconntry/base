package com.needto.account.model;

import com.needto.account.entity.Price;
import com.needto.dao.models.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 增值服务
 */
public class ValueAddService extends BaseEntity {

    public static final String TABLE = "_vas";

    /**
     * 名称
     */
    public String name;

    /**
     * 描述
     */
    public String desc;

    /**
     * 特性说明
     */
    public List<String> items;

    /**
     * 服务key
     */
    public String key;

    /**
     * 价格配置
     */
    public Map<String, Price> priceMap;
    /**
     * 有效时间
     */
    public Long expire;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public Map<String, Price> getPriceMap() {
        return priceMap;
    }

    public void setPriceMap(Map<String, Price> priceMap) {
        this.priceMap = priceMap;
    }
}
