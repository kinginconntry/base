package com.needto.cluster.service;

import com.needto.zk.service.ZkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Administrator
 */
public class ClusterService {

    @Autowired
    protected ZkService zkService;

    @Value("${spring.application.name}")
    protected String appName;

    @Value("${server.port}")
    protected String appPort;

    @Autowired
    protected Environment environment;

    public String getRootPath(){
        return "/" + this.appName;
    }

    public String domain() throws UnknownHostException {
        String ip = environment.getProperty("public.network.ip");
        if(StringUtils.isEmpty(ip)){
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        return ip;
    }
}
