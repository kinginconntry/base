package com.needto.organization;

import com.needto.common.context.GlobalEnv;
import com.needto.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * 默认的用户共用配置信息
 */
@RestController
public class OrganitionApi {

    @Autowired
    private OrganitionService organitionService;


    /**
     * 查找组织数据
     * @return
     */
    @PostMapping(value = {"/app/organition/find", "/sys/organition/find", "/admin/organition/find"})
    @ResponseBody
    public Result<Organition> findOrientation(){
        return Result.forSuccess(organitionService.find(GlobalEnv.getOwner()));
    }

    /**
     * 管理员端保存
     * @param organition
     * @return
     */
    @PostMapping(value = {"/sys/organition/save", "/admin/organition/save", "/app/organition/save"})
    @ResponseBody
    public Result<Organition> save(@RequestBody Organition organition){
        if(organition == null){
            return Result.forError("NO_DATA", "");
        }
        organition.setOwner(GlobalEnv.getOwner());
        return Result.forSuccess(organitionService.save(organition));
    }

}
