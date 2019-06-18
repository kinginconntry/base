package com.needto.zk.cluster;


import com.needto.zk.entity.Node;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 服务端节点管理服务
 */
public class ServerService extends ClusterService implements Watcher {

    private Map<String, Node> map = new HashMap<>();

    public void updateNodeInfo(){
        List<String> childrenPaths = zkService.getChildren(getRootPath());
        if(!CollectionUtils.isEmpty(childrenPaths)){
            childrenPaths.forEach(v -> {
                Node node = zkService.getData(v, Node.class);
                map.put(v, node);
            });
        }
    }


    @Override
    public void process(WatchedEvent event) {
        synchronized (this){
            System.out.println("Enter the process method,the event is :" + event);
            if(map.containsKey(event.getPath())){
                try {
                    Node node = zkService.getData(event.getPath(), Node.class);
                    map.put(event.getPath(), node);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<String, Node> getNodes(){
        return this.map;
    }
}
