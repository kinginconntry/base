package com.needto.notice.controller;

import com.needto.common.context.GlobalEnv;
import com.needto.notice.model.Notice;
import com.needto.notice.service.NoticeCache;
import com.needto.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接收消息
 */
@RestController
public class NoticeController {

    @Autowired
    private NoticeCache noticeCache;

    @Autowired
    private NoticeService noticeService;

    /**
     * 查找消息
     * @param request
     * @return
     */
    @RequestMapping(value = {"/sys/notice/find", "/admin/notice/find", "/app/notice/find"})
    @ResponseBody
    public Result<List<Notice>> findNotice(HttpServletRequest request){
        return Result.forSuccess(noticeCache.getNotices(GlobalEnv.getClient(request)));
    }

    /**
     * ack消息，标识消息已读
     * @param request
     * @return
     */
    @RequestMapping(value = {"/sys/notice/ack", "/admin/notice/ack", "/app/notice/ack"})
    @ResponseBody
    public Result<Long> ackNotice(HttpServletRequest request, @RequestBody List<String> ids){
        if(CollectionUtils.isEmpty(ids)){
            return Result.forError("NO_ID", "");
        }
        return Result.forSuccess(noticeService.ackNotice(GlobalEnv.getClient(request), ids));
    }
}
