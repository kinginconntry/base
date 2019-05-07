package com.needto.common.services.group;

import com.needto.common.dao.models.BaseEntity;
import com.needto.common.entity.Dict;
import com.needto.common.entity.Itree;
import com.needto.common.entity.Target;

/**
 * 分组实体
 * @author Administrator
 */
public class Group extends BaseEntity implements Itree {

    public static final String TABLE = "_group";

    /**
     * 主用户id
     */
    private String owner;

    /**
     * 父节点id
     */
    private String pid;

    /**
     * 所属
     */
    private Target belongto;

    /**
     * 分组名
     */
    private String name;

    /**
     * 分组描述
     */
    private String desc;

    /**
     * 图标
     */
    private String icon;

    /**
     * 数据
     */
    private Dict data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Dict getData() {
        return data;
    }

    public void setData(Dict data) {
        this.data = data;
    }

    @Override
    public String getCode() {
        return this.id;
    }

    @Override
    public String getPcode() {
        return this.pid;
    }
}
