package com.needto.zk.cluster;


import com.needto.zk.entity.Node;
import org.apache.zookeeper.CreateMode;

import java.net.UnknownHostException;

/**
 * @author Administrator
 * 客户端节点服务
 */
public class ClientService extends ClusterService {

    /**
     * 注册节点信息
     * @param data
     * @throws UnknownHostException
     */
    public void register(Object data) throws UnknownHostException {
        String domain = domain();
        Node note = new Node();
        note.setAppName(appName);
        note.setDomain(appPort);
        note.setDomain(domain);
        note.setData(data);
        String serviceInstance = "prometheus" + "-" +  domain + "-";
        String path = getRootPath() + "/" + serviceInstance;
        zkService.create(path, note, CreateMode.EPHEMERAL_SEQUENTIAL);
    }
}
