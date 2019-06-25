package com.needto.discount.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 打折结果
 */
public class DiscountMultiResult {

    /**
     * 多个单品折扣
     */
    public List<DiscountUnitResult> discountUnitResults;

    /**
     * 总折扣
     */
    public BigDecimal total;

    public List<DiscountUnitResult> getDiscountUnitResults() {
        return discountUnitResults;
    }

    public void setDiscountUnitResults(List<DiscountUnitResult> discountUnitResults) {
        this.discountUnitResults = discountUnitResults;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void addUnitResult(DiscountUnitResult discountUnitResult){
        if(discountUnitResult.isSuccess()){
            if(this.total == null){
                this.total = new BigDecimal("0");
            }
            total.add(discountUnitResult.getFee());
        }
        if(this.discountUnitResults == null){
            this.discountUnitResults = new ArrayList<>();
        }
        this.discountUnitResults.add(discountUnitResult);
    }
}
