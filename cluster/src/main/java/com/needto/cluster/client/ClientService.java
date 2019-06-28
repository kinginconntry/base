package com.needto.cluster.client;


import com.needto.cluster.entity.Node;
import com.needto.cluster.service.ClusterService;
import com.needto.tool.utils.Utils;
import org.apache.zookeeper.CreateMode;

import java.net.UnknownHostException;

/**
 * @author Administrator
 * 客户端节点服务
 */
public class ClientService extends ClusterService {

    /**
     * 注册自我节点信息
     * @param data
     * @throws UnknownHostException
     */
    public void registerSelf(Object data) throws UnknownHostException {
        Node node = new Node();
        node.setAppName(appName);
        node.setDomain(appPort);
        node.setDomain(domain());
        node.setData(data);
        registerNode(node, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public void registerNode(Node node, CreateMode createMode){
        String serviceInstance = Utils.join("", "prometheus", node.getAppName(), node.getDomain(), node.getPort());
        String path = getRootPath() + "/" + serviceInstance;
        zkService.create(path, node, createMode);
    }
}
