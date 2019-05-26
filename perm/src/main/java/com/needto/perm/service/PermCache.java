package com.needto.perm.service;

import com.google.common.collect.Lists;
import com.needto.cache.entity.CacheData;
import com.needto.cache.entity.CacheWrap;
import com.needto.cache.redis.RedisCache;
import com.needto.common.entity.Target;
import com.needto.perm.data.PermData;
import com.needto.perm.event.DataPermChangeEvent;
import com.needto.perm.event.RoleDataChangeEvent;
import com.needto.perm.event.TargetRoleChangeEvent;
import com.needto.perm.model.Role;
import com.needto.perm.model.RoleRelation;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PermCache {

    private static final String KEY_PREFIX = "_notice";

    @Autowired
    private Environment environment;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DataPermService dataPermService;

    @Autowired
    private FunctionPermService functionPermService;

    /**
     * 过期时间
     */
    private long expire;

    @PostConstruct
    public void init(){
        // 默认一天过期
        this.expire = Long.valueOf(environment.getProperty("perm.expire", "86400000"));
    }

    private String getKey(String owner, Target belongto){
        return redisCache.buildKey(KEY_PREFIX, owner, belongto.getTargetId());
    }

    private PermData getData(String owner, Target belongto){
        List<Role> roles = roleService.findByOwnerAndBelongTo(owner, belongto);
        Set<String> functionPermIds = new HashSet<>();
        Set<String> dataPermIds = new HashSet<>();
        for(Role role : roles){
            if(role.getFunctionPerms() != null){
                functionPermIds.addAll(role.getFunctionPerms());
            }
            if(role.getDataperms() != null){
                dataPermIds.addAll(role.getDataperms());
            }
        }
        PermData permData = new PermData();
        permData.dataPermEntities = dataPermService.getDataPermEntitysByIds(new ArrayList<>(dataPermIds));
        permData.funcperms = functionPermService.getCodesByIds(new ArrayList<>(functionPermIds));
        return permData;
    }

    public PermData getPermData(String owner, Target belongto){
        Assert.validateNull(belongto);
        Assert.validateStringEmpty(owner);

        CacheWrap<PermData> res = this.redisCache.get(getKey(owner, belongto), expire, new CacheData<CacheWrap<PermData>>() {
            @Override
            public CacheWrap<PermData> get() {
                PermData permData = getData(owner, belongto);
                return CacheWrap.wrap(permData);
            }
        });
        return res.getData();
    }

    @EventListener
    public void dataPermChage(DataPermChangeEvent dataPermChangeEvent){
        List<Role> roles = this.roleService.findByDataPerms(dataPermChangeEvent.dataPermIds);
        List<String> roleIds = new ArrayList<>();
        roles.forEach(v -> roleIds.add(v.getId()));
        removByRoleId(roleIds);
    }

    @EventListener
    public void roleDataChange(RoleDataChangeEvent roleDataChangeEvent){
        removByRoleId(roleDataChangeEvent.roleIds);
    }

    @EventListener
    public void targetRoleChage(TargetRoleChangeEvent targetRoleChangeEvent){
        remove(Lists.newArrayList(
                getKey(targetRoleChangeEvent.owner, targetRoleChangeEvent.target)
        ));
    }

    private void removByRoleId(List<String> roleIds){
        List<RoleRelation> roleRelations = roleService.findByRoleIds(roleIds);
        List<String> keys = new ArrayList<>();
        roleRelations.forEach(v -> keys.add(getKey(v.getOwner(), v.getBelongto())));
        remove(keys);
    }

    private void remove(List<String> keys){
        if(CollectionUtils.isEmpty(keys)){
            return;
        }
        List<String> tempKeys = new ArrayList<>(keys.size());
        keys.forEach(v -> tempKeys.add(redisCache.buildKey(KEY_PREFIX, v)));
        this.redisCache.remove(tempKeys);
    }

}
