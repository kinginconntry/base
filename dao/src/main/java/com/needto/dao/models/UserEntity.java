package com.needto.dao.models;

import org.springframework.util.StringUtils;

/**
 * @author Administrator
 * 跟登录用户相关的类
 */
public class UserEntity extends BaseEntity {

    /**
     * 创建者字段
     */
    public static final transient String CUSER = "cuser";

    /**
     * 更新者字段
     */
    public static final transient String UPUSER = "upuser";

    /**
     * 创建者
     */
    protected String cuser;

    /**
     * 更新者
     */
    protected String upuser;

    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public String getUpuser() {
        return upuser;
    }

    public void setUpuser(String upuser) {
        this.upuser = upuser;
    }

    public void initUser(String user){
        this.upuser = user;
        if(StringUtils.isEmpty(this.id)){
            this.cuser = user;
        }
    }
}
