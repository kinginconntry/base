package com.needto.common.inter;

import com.needto.common.data.Constant;
import com.needto.common.entity.Target;
import com.needto.common.utils.RequestUtil;
import com.needto.tool.entity.Dict;
import com.needto.tool.utils.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * 客户端缓存
 */
public interface IClientCache {

    /**
     * 获取客户端缓存唯一id
     * @param type
     * @param guid
     * @return
     */
    default String getGuid(String type, String guid){
        return type + "_" + guid;
    }

    /**
     * 获取客户端缓存唯一id
     * @param target
     * @return
     */
    default String getGuid(Target target){
        return target.getType() + "_" + target.getGuid();
    }

    /**
     * 设置客户端指纹
     * @param client
     */
    Target refresh(Target client);

    /**
     * 设置客户端指纹
     * @param httpServletRequest
     */
    default Target refresh(HttpServletRequest httpServletRequest){
        Target client = RequestUtil.getRequestFingerPrint(httpServletRequest);
        if(get(client) != null){
            return client;
        }
        return refresh(client);
    }

    /**
     * 获取客户端的信息对象
     * @param client
     * @return
     */
    Dict get(Target client);

    /**
     * 获取客户端的信息对象
     * @param httpServletRequest
     * @return
     */
    default Dict get(HttpServletRequest httpServletRequest){
        Target client = (Target) httpServletRequest.getAttribute(Constant.FINGER_KEY);
        return get(client);
    }

    /**
     * 主动移除客户端信息（被动等缓存过期）
     * @param client
     */
    void remove(Target client);

    /**
     * 主动移除客户端信息（被动等缓存过期）
     * @param httpServletRequest
     */
    default void remove(HttpServletRequest httpServletRequest){
        Target client = (Target) httpServletRequest.getAttribute(Constant.FINGER_KEY);
        if(client != null){
            remove(client);
        }
    }

    /**
     * 从客户端缓存中获取客户端属性
     * @param httpServletRequest
     * @param key
     * @param <T>
     * @return
     */
    default <T> T getValue(HttpServletRequest httpServletRequest, String key){
        Dict dict = get(httpServletRequest);
        return dict.getValue(key);
    }

    default <T> T getValue(Target target, String key){
        Dict dict = get(target);
        return dict.getValue(key);
    }

    /**
     * 将客户端属性设置到客户端信息缓存中
     * @param dict
     * @param key
     * @param value
     */
    default void setValue(Dict dict, String key, Object value){
        Assert.validateStringEmpty(key);
        dict.put(key, value);
    }

    default void setValue(Target target, String key, Object value){
        Assert.validateStringEmpty(key);
        Dict dict = get(target);
        dict.put(key, value);
    }

    /**
     * 将客户端属性设置到客户端信息缓存中
     * @param httpServletRequest
     * @param key
     * @param value
     */
    default void setValue(HttpServletRequest httpServletRequest, String key, Object value){
        Dict dict = get(httpServletRequest);
        setValue(dict, key, value);
    }

}
