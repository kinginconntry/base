package com.needto.config;

import com.google.common.collect.Lists;
import com.needto.common.entity.Target;
import com.needto.common.exception.ValidateException;
import com.needto.common.utils.Assert;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.FieldFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
        if(this.findConfigCatByKey(config.getCatId(), ConfigCat.class) == null){
            throw new ValidateException("NO_CAT", "");
        }
        if(this.findConfigByKey(config.getCatId(), config.getKey(), config.getClass()) != null){
            throw new ValidateException("EXISTED", "");
        }
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

    public <T extends Config> List<T> findConfigs(String catId, Class<T> objClass){
        if(StringUtils.isEmpty(catId)){
            return new ArrayList<>(0);
        }
        return this.mongoDao.find(FieldFilter.single("catId", catId), objClass, Config.TABLE);
    }

    public long deleteConfigByKey(String catId, String key){
        if(StringUtils.isEmpty(catId) || StringUtils.isEmpty(key)){
            return 0L;
        }
        List<FieldFilter> fieldFilters = Lists.newArrayList(new FieldFilter("catId", catId), new FieldFilter("key", key));
        return this.mongoDao.delete(fieldFilters, Config.TABLE);
    }

    public <T extends ConfigCat> T saveConfigCat(T configCat){
        if(StringUtils.isEmpty(configCat.getName())){
            throw new ValidateException("NO_NAME", "");
        }
        if(this.findConfigCatByKey(configCat.getId(), configCat.getClass()) != null){
            throw new ValidateException("EXISTED", "");
        }
        return mongoDao.save(configCat, ConfigCat.TABLE);
    }

    public <T extends ConfigCat> T findConfigCatByKey(String id, Class<T> objClass){
        if(StringUtils.isEmpty(id)){
            return null;
        }
        return this.mongoDao.findById(id, objClass, Config.TABLE);
    }

    public <T extends ConfigCat> List<T> findConfigCats(Target belongto, Class<T> objClass){
        Assert.validateNull(belongto);
        Assert.validateStringEmpty(belongto.getGuid());
        Assert.validateStringEmpty(belongto.getType());
        return this.mongoDao.find(Lists.newArrayList(

        ), objClass, Config.TABLE);
    }

    public long deleteConfigCat(String id){
        if(StringUtils.isEmpty(id)){
            return 0L;
        }
        return this.mongoDao.deleteById(id, ConfigCat.TABLE);
    }
}
