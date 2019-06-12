package com.needto.simulate.entity;

import com.needto.tool.inter.Thing;

/**
 * @author Administrator
 * 验证码识别接口
 */
public interface IValidateCode extends Thing {
    /**
     * 获取验证信息
     * @param guid 本次验证的唯一id
     * @param data 验证数据（验证码bs64，手机号，邮箱号）
     * @return
     */
    String getValidateCode(String guid, Object data);
}
