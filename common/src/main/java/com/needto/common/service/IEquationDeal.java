package com.needto.common.service;


import com.needto.common.utils.Assert;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
public interface IEquationDeal {

    /**
     * 算式符号
     * @return
     */
    String symbol();

    /**
     * 默认计算参数
     * @return
     */
    default int getParamLen(){ return 2; }

    /**
     * 默认优先级，优先级越高，越先计算
     * @return
     */
    default int priority(){
        return 0;
    }

    /**
     * 计算
     * @param params
     * @return
     */
    BigDecimal cal(BigDecimal... params);

    class Add implements IEquationDeal{

        @Override
        public String symbol() {
            return "+";
        }

        @Override
        public BigDecimal cal(BigDecimal... params) {
            return params[0].add(params[1]);
        }
    }

    class Sub implements IEquationDeal{

        @Override
        public String symbol() {
            return "-";
        }

        @Override
        public BigDecimal cal(BigDecimal... params) {
            return params[0].subtract(params[1]);
        }
    }

    class Multi implements IEquationDeal{

        @Override
        public String symbol() {
            return "*";
        }

        @Override
        public int priority() {
            return 1;
        }

        @Override
        public BigDecimal cal(BigDecimal... params) {
            return params[0].multiply(params[1]);
        }
    }

    class Devide implements IEquationDeal{

        @Override
        public String symbol() {
            return "/";
        }

        @Override
        public int priority() {
            return 1;
        }

        /**
         * 保留十位有效数据，遇到循环数时进行四舍五入
         * @param params
         * @return
         */
        @Override
        public BigDecimal cal(BigDecimal... params) {
            Assert.validateCondition(params[1].doubleValue() == 0);
            return params[0].divide(params[1], 10, BigDecimal.ROUND_HALF_UP);
        }
    }

}
