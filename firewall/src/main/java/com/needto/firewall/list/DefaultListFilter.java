package com.needto.firewall.list;

import com.needto.common.entity.Target;
import com.needto.tool.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * 默认的名单拦截器
 */
@Service
public class DefaultListFilter implements IListFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultListFilter.class);

    @Autowired
    private ListCache listCache;

    @Override
    public Result<Void> filter(String mode, Target target) {
        ListCache.Data data = listCache.get(mode, target);
        if(data == null){
            return Result.forError();
        }
        long now = System.currentTimeMillis();
        if(now >= data.start && data.end != null && now <= data.end){
            return Result.forSuccess();
        }
        return Result.forError();
    }
}
