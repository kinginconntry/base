package com.needto.pay.entity;

import java.math.BigDecimal;

public class PayData implements IPayData {

    public String guid;

    public BigDecimal bigDecimal;

    @Override
    public String guid() {
        return guid;
    }

    @Override
    public BigDecimal ammount() {
        return bigDecimal;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
}
