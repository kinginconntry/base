package com.needto.chatpush.ding;

import com.needto.http.utils.ApiRequest;
import com.needto.tool.inter.IValidate;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Administrator
 * 机器人推送服务
 */
@Service
public class DingPushService {

    private static final Logger LOG = LoggerFactory.getLogger(DingPushService.class);

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
            ApiRequest.post(rebotUrl, null, dingMsg, null, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LOG.debug("钉钉推送消息失败，实体 {}", dingMsg.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    LOG.debug("钉钉推送消息成功，实体 {}", dingMsg.toString());
                }
            });
            return true;
        } catch (Exception e) {
            //异常没有什么用，不log了。
        }
        return false;
    }
}
