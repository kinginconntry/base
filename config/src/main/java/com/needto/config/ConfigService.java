package com.needto.config;

import com.google.common.collect.Lists;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.FieldFilter;
import com.needto.tool.entity.Filter;
import com.needto.tool.entity.Order;
import com.needto.tool.exception.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Administrator
 * 配置服务，需要扫描启用
 */
@Service
public class ConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    private CommonDao mongoDao;

    public <T extends Config> T saveConfig(T config){
        if(StringUtils.isEmpty(config.getCatId())){
            throw new ValidateException("NO_CATID", "");
        }
        if(StringUtils.isEmpty(config.getName())){
            throw new ValidateException("NO_NAME", "");
        }
        if(StringUtils.isEmpty(config.getKey())){
            throw new ValidateException("NO_KEY", "");
        }
        if(this.findConfigByKey(config.getCatId(), config.getKey(), config.getClass()) != null){
            throw new ValidateException("EXISTED", "");
        }
        LOG.debug("保存配置");
        return mongoDao.save(config, Config.TABLE);
    }

    public <T extends Config> T findConfigByKey(String catId, String key, Class<T> objClass){
        if(StringUtils.isEmpty(catId) || StringUtils.isEmpty(key)){
            return null;
        }
        return this.mongoDao.findOne(Lists.newArrayList(
                new FieldFilter("catId", catId),
                new FieldFilter("key", key)
        ), objClass, Config.TABLE);
    }

    public <T extends Config> List<T> findConfig(List<Filter> filters, List<Order> orders, Class<T> objClass){

        return this.mongoDao.find(CommonQueryUtils.getFilters(filters), CommonQueryUtils.getOrders(orders), objClass, Config.TABLE);
    }

    public long deleteConfig(List<Filter> filters){
        LOG.debug("删除配置");
        return this.mongoDao.delete(CommonQueryUtils.getFilters(filters), Config.TABLE);
    }

    public <T extends ConfigCat> T saveConfigCat(T configCat){
        if(StringUtils.isEmpty(configCat.getName())){
            throw new ValidateException("NO_NAME", "");
        }
        if(this.findConfigCatByKey(configCat.getId(), configCat.getClass()) != null){
            throw new ValidateException("EXISTED", "");
        }
        LOG.debug("保存配置分类");
        return mongoDao.save(configCat, ConfigCat.TABLE);
    }

    public <T extends ConfigCat> T findConfigCatByKey(String id, Class<T> objClass){
        if(StringUtils.isEmpty(id)){
            return null;
        }
        return this.mongoDao.findById(id, objClass, Config.TABLE);
    }

    public long deleteConfigCat(List<Filter> filters){
        LOG.debug("删除配置分类");
        return this.mongoDao.delete(CommonQueryUtils.getFilters(filters), ConfigCat.TABLE);
    }
}
