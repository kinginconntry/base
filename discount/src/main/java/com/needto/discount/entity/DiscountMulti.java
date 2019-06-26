package com.needto.discount.entity;

import com.needto.tool.entity.Dict;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 折扣配置
 */
public class DiscountMulti {

    /**
     * 多个折扣配置
     */
    public List<DiscountUnit> discountUnitList;

    /**
     * 折扣配置
     */
    public Dict config;

    public List<DiscountUnit> getDiscountUnitList() {
        return discountUnitList;
    }

    public void setDiscountUnitList(List<DiscountUnit> discountUnitList) {
        this.discountUnitList = discountUnitList;
    }

    public void addDiscountUnitList(List<DiscountUnit> discountUnitList) {
        if(CollectionUtils.isEmpty(discountUnitList)){
            return;
        }
        if(this.discountUnitList == null){
            this.discountUnitList = new ArrayList<>();
        }
        this.discountUnitList.addAll(discountUnitList);
    }

    public void addDiscountUnit(DiscountUnit discountUnit) {
        if(discountUnit == null){
            return;
        }
        if(this.discountUnitList == null){
            this.discountUnitList = new ArrayList<>();
        }
        this.discountUnitList.add(discountUnit);
    }

    public Dict getConfig() {
        return config;
    }

    public void setConfig(Dict config) {
        this.config = config;
    }
}
