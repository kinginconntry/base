package com.needto.common.bus.logout;

import com.needto.common.entity.Target;
import com.needto.common.services.eventbus.distribute.RedisEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    @Autowired
    private RedisEventBus redisEventBus;

    /**
     * 发布广播消息，用户退出登录
     */
    public void logout(Target target){
        Message message = new Message();
        message.setTarget(target);
        redisEventBus.sendMsg("logout", message);
    }
}
