package com.needto.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 多条件校验器：判断多个条件成立关系
 */
public class ConditionValidate {

    public List<Boolean> conditions;

    public ConditionValidate(){
        this.conditions = new ArrayList<>();
    }

    public ConditionValidate(Boolean flag){
        this();
        this.conditions.add(flag);
    }

    public static ConditionValidate create(){
        return new ConditionValidate();
    }

    public static ConditionValidate create(Boolean flag){
        return new ConditionValidate(flag);
    }

    public void add(Boolean flag){
        this.conditions.add(flag);
    }

    public boolean and(){
        for(Boolean temp : this.conditions){
            if(temp == null || !temp){
                return false;
            }
        }
        return true;
    }

    public boolean or(){
        for(Boolean temp : this.conditions){
            if(temp){
                return true;
            }
        }
        return false;
    }
}
