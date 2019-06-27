package com.needto.account.service;

import com.needto.account.model.LevelConfig;
import com.needto.account.model.ValueAddService;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.inter.CommonDao;
import com.needto.tool.entity.Filter;
import com.needto.tool.entity.Order;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * 服务管理器
 */
@Service
public class AppFeatureServiceManager {

    @Autowired
    private CommonDao commonDao;

    /**
     * 获取所有版本
     * @return
     */
    public List<LevelConfig> getLevelConfig(List<Filter> filters, List<Order> orders){
        List<LevelConfig> list = commonDao.find(CommonQueryUtils.getFilters(filters), CommonQueryUtils.getOrders(orders), LevelConfig.class, LevelConfig.TABLE);
        list.sort((a, b) -> {
            if(a.getOrder() > b.getOrder()){
                return 1;
            }else if(a.getOrder() < b.getOrder()){
                return -1;
            }else{
                return 0;
            }
        });
        return list;
    }

    /**
     * 获取版本
     * @param key
     * @return
     */
    public LevelConfig getLevelConfig(String key){
        return this.commonDao.findOne(FieldFilter.single("key", key), LevelConfig.class, LevelConfig.TABLE);
    }

    /**
     * 获取默认版本
     * @return
     */
    public LevelConfig getDefaultLevelConfig(){
        return this.commonDao.findOne(FieldFilter.single("dft", true), LevelConfig.class, LevelConfig.TABLE);
    }

    public LevelConfig findOneLevelConfig(List<Filter> filters){
        return this.commonDao.findOne(CommonQueryUtils.getFilters(filters), LevelConfig.class, LevelConfig.TABLE);
    }

    public long deleteLevelConfig(List<Filter> filters){
        return this.commonDao.delete(CommonQueryUtils.getFilters(filters), LevelConfig.TABLE);
    }

    /**
     * 保存版本
     * @param levelConfig
     */
    public LevelConfig saveLevelConfig(LevelConfig levelConfig){
        Assert.validateNull(levelConfig);
        Assert.validateStringEmpty(levelConfig.getKey());
        Assert.validateStringEmpty(levelConfig.getName());
        LevelConfig old = getLevelConfig(levelConfig.getKey());
        if(old != null){
            levelConfig.setId(old.getId());
            levelConfig.setCtime(old.getCtime());
        }
        if(levelConfig.isDft()){
            LevelConfig dft = getDefaultLevelConfig();
            if(dft != null){
                dft.setDft(false);
                this.commonDao.save(dft, LevelConfig.TABLE);
                return dft;
            }
        }
        this.commonDao.save(levelConfig, LevelConfig.TABLE);
        return levelConfig;
    }

    /**
     * 获取所有增值服务
     * @return
     */
    public List<ValueAddService> getValueAddService(List<Filter> filters, List<Order> orders){
        return this.commonDao.find(CommonQueryUtils.getFilters(filters), CommonQueryUtils.getOrders(orders), ValueAddService.class, ValueAddService.TABLE);
    }

    public long deleteValueAddService(List<Filter> filters){
        return this.commonDao.delete(CommonQueryUtils.getFilters(filters), ValueAddService.TABLE);
    }

    public ValueAddService findOneValueAddService(List<Filter> filters){
        return this.commonDao.findOne(CommonQueryUtils.getFilters(filters), ValueAddService.class, ValueAddService.TABLE);
    }

}
