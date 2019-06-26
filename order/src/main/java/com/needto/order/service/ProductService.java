package com.needto.order.service;

import com.needto.order.data.Product;

/**
 * @author Administrator
 */
public interface ProductService {

    <T extends Product> T getProduct(String key);
}
