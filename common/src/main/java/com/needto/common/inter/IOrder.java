package com.needto.common.inter;

/**
 * @author Administrator
 *
 */
public interface IOrder {
    /**
     * 实体顺序
     * @return
     */
    default int getOrder(){return 0;}
}
