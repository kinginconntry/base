package com.needto.zk.service;

import com.alibaba.fastjson.JSON;
import com.needto.zk.entity.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class ZkService {

    private static final String DEFAULT_CHARSET = "utf-8";

    private final static Logger logger = LoggerFactory.getLogger(ZkService.class);

    @Autowired
    private ZkClient zkClient;

    /**
     * 创建节点
     * @param path
     * @param data
     * @param createMode
     * @return
     */
    public boolean create(String path, Object data, CreateMode createMode){
        try {
            if(data == null){
                zkClient.getClient().create().orSetData().creatingParentsIfNeeded().withMode(createMode).forPath(path);
            }else{
                zkClient.getClient().create().orSetData().creatingParentsIfNeeded().withMode(createMode).forPath(path, JSON.toJSONString(data).getBytes(DEFAULT_CHARSET));
            }
            return true;
        } catch (Exception e) {
            logger.error("保存出错", e);
        }
        return false;
    }

    /**
     * 设置节点数据
     * @param path
     * @param data
     * @return
     */
    public boolean setData(String path, Object data) {
        try {
            if(!exists(path)){
                return false;
            }
            zkClient.getClient().setData().forPath(path, JSON.toJSONString(data).getBytes(DEFAULT_CHARSET));
            return true;
        } catch (Exception e) {
            logger.error("保存出错", e);
        }
        return false;
    }

    /**
     * 获取节点数据
     * @param path
     * @param objClass
     * @param <T>
     * @return
     */
    public <T> T getData(String path, Class<T> objClass){
        try {
            if(!exists(path)){
                return null;
            }
            byte[] bytes = zkClient.getClient().getData().forPath(path);
            if(bytes == null){
                return null;
            }
            return JSON.parseObject(new String(bytes, DEFAULT_CHARSET), objClass);
        } catch (Exception e) {
            logger.error("获取出错", e);
        }
        return null;
    }

    public boolean delete(String path){
        try {
            zkClient.getClient().delete().forPath(path);
            return true;
        }catch (Exception e){
            logger.error("删除出错", e);
        }
        return false;
    }

    public boolean exists(String path){
        try {
            Stat stat = zkClient.getClient().checkExists().forPath(path);
            if(stat == null){
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            logger.error("检查存在出错", e);
        }
        return false;

    }

    public List<String> getChildren(String path) {
        List<String> childrenList = new ArrayList<>();
        try {
            childrenList = zkClient.getClient().getChildren().forPath(path);
        } catch (Exception e) {
            logger.error("获取子节点出错", e);
        }
        return childrenList;
    }

    public int getChildrenCount(String path) {
        return getChildren(path).size();
    }


}
