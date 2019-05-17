package com.needto.server;

import com.needto.common.utils.Utils;
import com.needto.zk.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.lang.Thread.sleep;

/**
 * @author Administrator
 */
@Component
public class ZkDynamicServer implements IDynamicServer {

    private static final Logger LOG = LoggerFactory.getLogger(ZkDynamicServer.class);

    @Autowired
    private Environment environment;

    private String path;

    @Autowired
    private ZkClient zkClient;

    @PostConstruct
    public void init() {
        path = environment.getProperty("dynamic.path", "dynamicSign");
        long sleepMs = Long.valueOf(environment.getProperty("dynamic.sleepMs", "7200000"));
        Boolean startTask = Boolean.valueOf(environment.getProperty("dynamic.start", "false"));
        this.updateSign();
        if(startTask){
            ((Runnable) () -> {
                while (true) {
                    try {
                        sleep(sleepMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateSign();
                }
            }).run();
        }
    }

    @Override
    public void updateSign() {
        try{
            byte[] data = zkClient.getClient().getData().forPath(path);
            if (data == null) {
                zkClient.getClient().setData().forPath(path, Utils.getGuid().getBytes("utf-8"));
                LOG.debug("更新内部token");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
