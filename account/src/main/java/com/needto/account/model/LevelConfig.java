package com.needto.account.model;

import com.needto.account.entity.Price;
import com.needto.dao.models.BaseEntity;
import com.needto.tool.entity.Dict;
import com.needto.tool.inter.IOrder;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 版本配置
 */
public class LevelConfig extends BaseEntity implements IOrder {

    public static final String TABLE = "_levelConfig";

    /**
     * 特性
     */
    public static class Feature{
        /**
         * 特性键
         */
        public String key;
        /**
         * 特性值
         */
        public Dict value;

        /**
         * 有效时间
         */
        public Long expire;
    }

    public static class Resource {

        /**
         * 资源键
         */
        public String key;

        /**
         * 最大值
         */
        public Integer max;

        /**
         * 有效时间
         */
        public Long expire;
    }

    /**
     * 版本号
     */
    public String key;

    /**
     * 版本名
     */
    public String name;

    /**
     * 版本描述
     */
    public String desc;

    /**
     * 是否为默认版本，用户在初始化时使用的版本
     */
    public boolean dft;

    /**
     * 版本资源限制，内部使用
     */
    public List<Resource> resources;

    /**
     * 特性，内部使用
     */
    public List<Feature> features;

    /**
     * 最大成员数, 内部使用
     */
    public Long maxMemberNum;

    /**
     * 基于某个版本(用于特性说明继承)
     */
    public String plevel;

    /**
     * 特性说明
     */
    public List<String> items;

    /**
     * 价格配置
     */
    public Map<String, Price> priceMap;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Long getMaxMemberNum() {
        return maxMemberNum;
    }

    public void setMaxMemberNum(Long maxMemberNum) {
        this.maxMemberNum = maxMemberNum;
    }

    public String getPlevel() {
        return plevel;
    }

    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    public boolean isDft() {
        return dft;
    }

    public void setDft(boolean dft) {
        this.dft = dft;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
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
