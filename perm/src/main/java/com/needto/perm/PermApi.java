package com.needto.perm;

import com.alibaba.fastjson.JSON;
import com.needto.common.entity.Query;
import com.needto.perm.data.FunctionPermData;
import com.needto.perm.model.FunctionPerm;
import com.needto.perm.model.Role;
import com.needto.perm.service.FunctionPermService;
import com.needto.perm.service.RoleService;
import com.needto.tool.entity.Result;
import com.needto.web.context.WebEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * 默认的资源权限控制器
 */
@RestController
public class PermApi {


    private static final Logger LOG = LoggerFactory.getLogger(PermApi.class);

    @Autowired
    private FunctionPermService permissionService;

    @Autowired
    private RoleService roleService;

    /**
     * 保存角色
     * @param role
     * @return
     */
    @PostMapping(value = {"/app/role/save", "/sys/role/save", "/admin/role/save"})
    @ResponseBody
    public Result<Role> save(@RequestBody Role role){
        return Result.forSuccessIfNotNull(roleService.save(role));
    }

    /**
     * 查找角色
     * @param query
     * @return
     */
    @PostMapping(value = {"/app/role/find", "/sys/role/find", "/admin/role/find"})
    @ResponseBody
    public Result<List<Role>> find(@RequestBody Query query){
        if(query == null){
            return Result.forSuccess();
        }
        return Result.forSuccess(this.roleService.find(WebEnv.getOwner(), query));
    }

    /**
     * 删除角色
     * @param ids
     * @return
     */
    @PostMapping(value = {"/app/role/delete", "/sys/role/delete", "/admin/role/delete"})
    @ResponseBody
    public Result<Long> delete(@RequestBody List<String> ids){
        if(CollectionUtils.isEmpty(ids)){
            return Result.forError("NO_ID", "");
        }
        long c = roleService.deleteByIds(WebEnv.getOwner(), ids);
        if(c == 0){
            return Result.forError();
        }else{
            return Result.forSuccess(c);
        }
    }

    /**
     * 保存权限
     * @param permission
     * @return
     */
    @PostMapping(value = {"/sys/role/save", "/admin/role/save"})
    @ResponseBody
    public Result<FunctionPerm> savePerm(@RequestBody FunctionPerm permission){
        LOG.debug("保存权限，操作人 {}", WebEnv.getOwner());
        return Result.forSuccessIfNotNull(permissionService.save(permission));
    }

    /**
     * 查找权限
     * @param query
     * @return
     */
    @PostMapping(value = {"/sys/perm/find", "/admin/perm/find"})
    @ResponseBody
    public Result<List<FunctionPerm>> findPerm(@RequestBody Query query){
        if(query == null){
            return Result.forSuccess();
        }
        return Result.forSuccess(this.permissionService.find(query));
    }

    /**
     * 查找权限
     * @return
     */
    @PostMapping(value = {"/app/permdata/find", "/sys/permdata/find", "/admin/permdata/find"})
    @ResponseBody
    public Result<List<FunctionPermData>> findPermData(){
        return Result.forSuccess(this.permissionService.getData());
    }

    /**
     * 删除权限
     * @param ids
     * @return
     */
    @PostMapping(value = {"/sys/perm/delete", "/admin/perm/delete"})
    @ResponseBody
    public Result<Long> deletePerm(@RequestBody List<String> ids){
        if(CollectionUtils.isEmpty(ids)){
            return Result.forError("NO_ID", "");
        }
        LOG.debug("保存权限，操作人 {}，删除ID {}", WebEnv.getOwner(), JSON.toJSONString(ids));
        long c = permissionService.deleteIds(ids);
        if(c == 0){
            return Result.forError();
        }else{
            return Result.forSuccess(c);
        }
    }


}
