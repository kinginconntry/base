package com.needto.common.services.resourceperm.entity;

import com.needto.common.dao.models.BaseEntity;
import com.needto.common.entity.Target;

/**
 * @author Administrator
 * 角色关系
 */
public class RoleRelation extends BaseEntity {

    public static final String TABLE = "_rolerelation";

    /**
     * 主账户id
     */
    public String owner;

    /**
     * 角色所属
     */
    public Target belongto;

    /**
     * 角色id
     */
    public String role;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Target getBelongto() {
        return belongto;
    }

    public void setBelongto(Target belongto) {
        this.belongto = belongto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
