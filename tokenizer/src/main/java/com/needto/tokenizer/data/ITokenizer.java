package com.needto.tokenizer.data;

import com.needto.tool.entity.Dict;
import com.needto.tool.inter.Thing;
import com.needto.tool.utils.Assert;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 * 分词接口
 * FIXME 同义词，量词，停顿词，扩展词库
 */
public interface ITokenizer extends Thing {
    /**
     * 返回所有分词结果（所有分词模式），去除重复
     * @param text
     * @return
     */
    default Set<String> getAll(String text, Dict config){
        Map<String, Set<String>> map = getMore(text, config);
        if(map == null){
            return new HashSet<>(0);
        }
        Set<String> res = new HashSet<>();
        for(Set<String> temp : map.values()){
            res.addAll(temp);
        }
        return res;
    }

    /**
     * 返回某个模式下的分词
     * @param text
     * @param mode
     * @return
     */
    default Set<String> get(String text, String mode, Dict config){
        Assert.validateStringEmpty(mode);
        Map<String, Set<String>> map = getMore(text, config);
        if(map == null){
            return new HashSet<>(0);
        }
        Set<String> res = map.get(mode);
        if(res == null){
            return new HashSet<>(0);
        }
        return res;
    }

    Map<String, Set<String>> getMore(String text, Dict config);

}
