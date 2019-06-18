package com.needto.zk.utils;

import com.needto.tool.utils.Assert;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * zk工具类
 */
public class ZkUtil {

    /**
     * 获取绝对路径
     * @param parentAbs 父节点路径
     * @param children 子节点路径
     * @return
     */
    public static String combine(String parentAbs, String children){

        Assert.validateCondition(StringUtils.isEmpty(children) || "/".equals(children));
        String tempPs = parentAbs;
        if(StringUtils.isEmpty(tempPs)){
            tempPs = "/";
        }
        if(!tempPs.startsWith("/")){
            tempPs = "/" + tempPs;
        }

        if(!tempPs.endsWith("/")){
            tempPs += "/";
        }

        if(children.startsWith("/")){
            children = children.substring(1);
        }
        return tempPs + children;
    }
}
