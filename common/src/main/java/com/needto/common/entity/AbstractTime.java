package com.needto.common.entity;

import com.needto.common.inter.IRelate;
import com.needto.common.inter.IValidate;

/**
 * @author Administrator
 * 时间数据
 */
abstract public class AbstractTime implements IRelate, IValidate {

    @Override
    public boolean coExist() {
        return false;
    }
}
