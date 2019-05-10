package com.needto.push.rebot.ding;

import com.needto.common.entity.Dict;
import com.needto.common.inter.IValidate;
import com.needto.common.utils.ApiRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * 机器人推送服务
 */
@Service
public class RebotPushService {

    private static final Logger LOG = LoggerFactory.getLogger(RebotPushService.class);

    /**
	 * 将需要推送的消息使用丁丁机器人推送
	 * @param dingMsg 发送的内容
	 * @param rebotUrl  机器人发送消息的链接
	 */
    public boolean dingRobotSend(String rebotUrl, IValidate dingMsg) {
        if(!dingMsg.validate()){
            return false;
        }
        try {
            ApiRequest.request(rebotUrl, HttpMethod.POST, dingMsg, Dict.class);
            LOG.debug("钉钉推送消息，实体 {}", dingMsg.toString());
            return true;
        } catch (Exception e) {
            //异常没有什么用，不log了。
        }
        return false;
    }
}
