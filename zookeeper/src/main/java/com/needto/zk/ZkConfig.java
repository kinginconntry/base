package com.needto.zk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Administrator
 */
@Configuration
public class ZkConfig {

    @Autowired
    private Environment environment;


    @Bean(initMethod = "init", destroyMethod = "stop")
    public ZkClient zkClient() {
        String zookeeperServer = environment.getProperty("zookeeper.server", "_default_");
        Integer sessionTimeoutMs = Integer.valueOf(environment.getProperty("zookeeper.sessionTimeoutMs", "1800000"));
        Integer connectionTimeoutMs = Integer.valueOf(environment.getProperty("zookeeper.connectionTimeoutMs", "60000"));
        Integer maxRetries = Integer.valueOf(environment.getProperty("zookeeper.maxRetries", "3"));
        Integer baseSleepTimeMs = Integer.valueOf(environment.getProperty("zookeeper.baseSleepTimeMs", "3000"));
        ZkClient zkClient = new ZkClient();
        zkClient.setZookeeperServer(zookeeperServer);
        zkClient.setSessionTimeoutMs(sessionTimeoutMs);
        zkClient.setConnectionTimeoutMs(connectionTimeoutMs);
        zkClient.setMaxRetries(maxRetries);
        zkClient.setBaseSleepTimeMs(baseSleepTimeMs);
        return zkClient;
    }
}
