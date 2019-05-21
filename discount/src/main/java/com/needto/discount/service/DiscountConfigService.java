package com.needto.discount.service;

import com.needto.common.entity.Filter;
import com.needto.common.entity.Target;
import com.needto.dao.common.CommonDao;
import com.needto.discount.model.DiscountConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * 折扣配置数据
 */
@Service
public class DiscountConfigService {

    @Autowired
    private CommonDao commonDao;

    public DiscountConfig save(DiscountConfig discountConfig){
        return commonDao.save(discountConfig, DiscountConfig.TABLE);
    }

    public long delete(List<String> ids, Target belong){
        return 0L;
    }

    public List<DiscountConfig> find(List<Filter> filters, Target belong){
        return null;
    }
}
