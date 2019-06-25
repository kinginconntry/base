package com.needto.perm.model;

import com.needto.common.entity.Target;
import com.needto.dao.models.BaseEntity;

import java.util.List;

/**
 * @author Administrator
 * 角色关系
 */
public class RoleRelation extends BaseEntity {

    public static final String TABLE = "_rolerelation";

    /**
     * 角色所属
     */
    public Target belongto;

    /**
     * 角色id集合
     */
    public List<String> roles;

    public Target getBelongto() {
        return belongto;
    }

    public void setBelongto(Target belongto) {
        this.belongto = belongto;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
