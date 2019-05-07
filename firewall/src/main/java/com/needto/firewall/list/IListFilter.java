package com.needto.firewall.list;

import com.needto.common.entity.Result;
import com.needto.common.entity.Target;

/**
 * @author Administrator
 * 名单拦截接口
 */
public interface IListFilter {

    /**
     * 名单拦截
     * @param mode 拦截模式
     * @param target 目标
     * @return Result#success为false则表示不需要拦截，Result#success为true则表示需要进行拦截，message和error和写明拦截的状态码和理由
     */
    default Result<Void> filter(String mode, Target target){ return Result.forError(); }
}
