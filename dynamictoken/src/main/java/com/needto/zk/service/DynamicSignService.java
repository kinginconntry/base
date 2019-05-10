package com.needto.zk.service;


import com.needto.common.utils.Utils;
import com.needto.zk.entity.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.lang.Thread.sleep;

/**
 * @author Administrator
 * 内部服务动态签名调用
 */
@Service
public class DynamicSignService implements Watcher {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicSignService.class);

    @Autowired
    private Environment environment;

    private String sign;

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
            new Runnable(){

                @Override
                public void run() {
                    try {
                        sleep(sleepMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateSign();

                }
            }.run();
        }
    }

    public void updateSign() {
        synchronized (this){
            try{
                byte[] data = zkClient.getClient().getData().forPath(path);
                if (data == null) {
                    this.sign = Utils.getGuid();
                    zkClient.getClient().setData().forPath(path, sign.getBytes("utf-8"));
                }else{
                    this.sign = new String(data, "utf-8");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

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
                        this.sign = new String(data, "utf-8");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getSign(){
        return this.sign;
    }
}
