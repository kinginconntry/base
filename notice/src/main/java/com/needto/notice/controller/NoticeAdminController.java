package com.needto.notice.controller;

import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.common.entity.Result;
import com.needto.notice.model.Notice;
import com.needto.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理
 */
@RestController
public class NoticeAdminController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 查找消息
     * @param query
     * @return
     */
    @RequestMapping(value = {"/sys/notice/manager/find", "/admin/notice/manager/find"})
    @ResponseBody
    public PageResult<List<Notice>> page(@RequestBody Query query){
        return noticeService.page(query);
    }

    /**
     * 创建广播
     * @param notice
     * @return
     */
    @RequestMapping(value = {"/sys/notice/manager/broadcast/create", "/admin/notice/manager/broadcast/create"})
    @ResponseBody
    public Result<Notice> createBroadCast(@RequestBody Notice notice){
        return Result.forSuccessIfNotNull(noticeService.createBroadCast(notice));
    }

    /**
     * 删除公告
     * @param ids
     * @return
     */
    @RequestMapping(value = {"/sys/notice/manager/delete", "/admin/notice/manager/delete"})
    @ResponseBody
    public Result<Long> delete(@RequestBody List<String> ids){
        return Result.forSuccessIfNotNull(noticeService.deleteByIds(ids));
    }
}
