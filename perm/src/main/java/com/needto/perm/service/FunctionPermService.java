package com.needto.perm.service;

import com.needto.common.entity.Query;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.FieldFilter;
import com.needto.perm.data.FunctionPermData;
import com.needto.perm.model.FunctionPerm;
import com.needto.perm.model.PermCat;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 资源权限service
 */
@Service
public class FunctionPermService {

    @Autowired
    private CommonDao commonDao;

    public PermCat save(PermCat permCat) {
        Assert.validateNull(permCat);
        Assert.validateStringEmpty(permCat.getName());
        return this.commonDao.save(permCat, PermCat.TABLE);
    }

    public FunctionPerm save(FunctionPerm permission) {
        Assert.validateNull(permission);
        Assert.validateStringEmpty(permission.getPerm(), "", "没有权限码");
        Assert.validateStringEmpty(permission.getName(), "", "没有权限名称");
        Assert.validateStringEmpty(permission.getCatId(), "", "没有权限分类");
        Assert.validateCondition(this.commonDao.findOne(FieldFilter.single("perm", permission.getPerm()), FunctionPerm.class, FunctionPerm.TABLE) != null, "权限码已存在，请更换");
        return this.commonDao.save(permission, FunctionPerm.TABLE);
    }

    public List<FunctionPerm> find(Query query) {
        return this.commonDao.find(CommonQueryUtils.getQuery(query), FunctionPerm.class, FunctionPerm.TABLE);
    }

    public List<String> getCodesByIds(List<String> ids){
        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<>(0);
        }
        List<FunctionPerm> functionPerms = this.commonDao.findByIds(ids, FunctionPerm.class, FunctionPerm.TABLE);
        List<String> codes = new ArrayList<>(functionPerms.size());
        functionPerms.forEach(functionPerm -> codes.add(functionPerm.getPerm()));
        return codes;
    }

    public long deleteIds(List<String> ids) {
        Assert.validateNull(ids);
        return this.commonDao.deleteByIds(ids, FunctionPerm.TABLE);
    }

    /**
     * 获取设置的权限数据
     * @return
     */
    public List<FunctionPermData> getData(){
        List<PermCat> permCats = this.commonDao.find(new ArrayList<>(0), PermCat.class, PermCat.TABLE);
        List<FunctionPerm> functionPerms = this.commonDao.find(new ArrayList<>(0), FunctionPerm.class, FunctionPerm.TABLE);
        List<FunctionPermData> functionPermDatas = new ArrayList<>(permCats.size() + 1);
        FunctionPermData defaultPerms = new FunctionPermData();
        Map<String, Integer> dict = new HashMap<>();
        for(PermCat permCat : permCats){
            functionPermDatas.add(new FunctionPermData());
            dict.put(permCat.getId(), functionPermDatas.size());
        }

        for(FunctionPerm functionPerm : functionPerms){
            Integer index = dict.get(functionPerm.getCatId());
            if(index == null){
                defaultPerms.addFunctionPerm(functionPerm);
            }else{
                functionPermDatas.get(index).addFunctionPerm(functionPerm);
            }
        }

        functionPermDatas.add(defaultPerms);
        return functionPermDatas;
    }

}
