package com.needto.server;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.needto.zk.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedDelaySchedule;
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
            new AbstractScheduledService(){
                @Override
                protected void runOneIteration() {
                    updateSign();
                }

                @Override
                protected Scheduler scheduler() {
                    return newFixedDelaySchedule(sleepMs, sleepMs, TimeUnit.MILLISECONDS);
                }

                @Override
                protected void startUp() throws Exception {
                    super.startUp();
                    LOG.debug("DynamicTask start ..........");
                }

                @Override
                protected void shutDown() throws Exception {
                    super.shutDown();
                    LOG.debug("DynamicTask start ..........");
                }
            };
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
