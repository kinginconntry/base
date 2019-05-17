package com.needto.user;

import com.needto.common.context.GlobalEnv;
import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * 开放日志
 */
@RestController
public class OpLogApi {

    @Autowired
    private OpLogService opLogService;

    /**
     * 查询日志
     * @param query
     * @return
     */
    @PostMapping(value = {"/app/oplog/page"})
    @ResponseBody
    public PageResult<List<OpLog>> page(@RequestBody Query query){
        return opLogService.findByPage(query, GlobalEnv.getOwner());
    }

    /**
     * 管理日志
     * @param query
     * @return
     */
    @PostMapping(value = {"/sys/oplog/page", "/admin/oplog/page"})
    @ResponseBody
    public PageResult<List<OpLog>> managerpage(@RequestBody Query query){
        return opLogService.findByPage(query);
    }

    /**
     * 删除日志
     * @param ids
     * @return
     */
    @PostMapping(value = {"/sys/oplog/delete", "/admin/oplog/delete"})
    @ResponseBody
    public Result<Long> delete(@RequestBody List<String> ids){
        long c = opLogService.delete(ids);

        if(c > 0){
            return Result.forSuccess(c);
        }else{
            return Result.forError();
        }
    }

}
