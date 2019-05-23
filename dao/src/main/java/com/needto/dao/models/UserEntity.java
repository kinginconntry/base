package com.needto.dao.models;

import com.needto.dao.inter.ICuser;
import com.needto.dao.inter.IUpuser;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 * 跟登录用户相关的类
 */
public class UserEntity extends BaseEntity implements ICuser, IUpuser {


    /**
     * 创建者
     */
    protected String cuser;

    /**
     * 更新者
     */
    protected String upuser;

    @Override
    public String getCuser() {
        return cuser;
    }

    @Override
    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    @Override
    public String getUpuser() {
        return upuser;
    }

    @Override
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
