package com.needto.cluster.server;


import com.google.common.collect.Lists;
import com.needto.cluster.entity.Node;
import com.needto.cluster.service.ClusterService;
import com.needto.tool.utils.ScheduleUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Administrator
 * 服务端节点管理服务
 */
@Service
public class ServerService extends ClusterService implements Watcher {

    private Map<String, Node> map = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        ScheduleUtil.start("node update", ScheduleUtil.Type.RATE, 0, 60000, () -> {
            updateNodeInfo();
            return true;
        });
    }

    /**
     * 更新节点信息
     */
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
            try {
                Node node = zkService.getData(event.getPath(), Node.class);
                map.put(event.getPath(), node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Node getNode(String domain, String port){
        AtomicReference<Node> node = new AtomicReference<>();
        this.map.forEach((k, v) ->{
            if(StringUtils.equals(v.getDomain(), domain) && StringUtils.equals(v.getPort(), port)){
                node.set(v);
                return;
            }
        });
        return node.get();
    }

    public List<Node> getAllNodes(){
        return Lists.newArrayList(this.map.values());
    }

    public List<Node> getGroupNodes(String group){
        List<Node> nodes = new ArrayList<>();
        this.map.forEach((k, v) -> {
            if(StringUtils.equals(v.getGroup(), group)){
                nodes.add(v);
            }
        });
        return nodes;
    }

    public List<Node> getAppNodes(String app){
        List<Node> nodes = new ArrayList<>();
        this.map.forEach((k, v) -> {
            if(StringUtils.equals(v.getGroup(), app)){
                nodes.add(v);
            }
        });
        return nodes;
    }

    public List<Node> getAppNodes(String app, String group){
        List<Node> nodes = new ArrayList<>();
        this.map.forEach((k, v) -> {
            if(StringUtils.equals(v.getGroup(), group) && StringUtils.equals(v.getAppName(), app)){
                nodes.add(v);
            }
        });
        return nodes;
    }
}
