package com.needto.client;


import com.needto.zk.entity.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @author Administrator
 * 内部服务动态签名调用，负责接口动态
 */
public class ZkDynamicClient implements Watcher, DynamicClient {

    private static final Logger LOG = LoggerFactory.getLogger(ZkDynamicClient.class);

    @Autowired
    private Environment environment;

    private String clientIde;

    private String path;

    @Autowired
    private ZkClient zkClient;

    @PostConstruct
    public void init() {
        path = environment.getProperty("dynamic.path", "dynamicSign");
    }

    @Override
    public boolean isValid(String sign){
        if(sign.contains(sign)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void process(WatchedEvent event) {
        synchronized (this){
            System.out.println("Enter the process method,the event is :" + event);
            if(path.equals(event.getPath())){
                try {
                    byte[] data = zkClient.getClient().getData().forPath(path);
                    if (data != null) {
                        this.clientIde = new String(data, "utf-8");
                        LOG.debug("更新客户端标识");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String getClientIde(){
        return this.clientIde;
    }
}
