package com.kuliao.oa.db.model;

public class RoleMenuOperation {
    private Integer id;

    private Integer roleMenuId;

    private Byte create;

    private Byte delete;

    private Byte update;

    private Byte select;

    public RoleMenuOperation() {
    }

    public RoleMenuOperation(Integer roleMenuId, Byte create, Byte delete, Byte update, Byte select) {
        this.roleMenuId = roleMenuId;
        this.create = create;
        this.delete = delete;
        this.update = update;
        this.select = select;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRoleMenuId() {
        return roleMenuId;
    }

    public void setRoleMenuId(Integer roleMenuId) {
        this.roleMenuId = roleMenuId;
    }

    public Byte getCreate() {
        return create;
    }

    public void setCreate(Byte create) {
        this.create = create;
    }

    public Byte getDelete() {
        return delete;
    }

    public void setDelete(Byte delete) {
        this.delete = delete;
    }

    public Byte getUpdate() {
        return update;
    }

    public void setUpdate(Byte update) {
        this.update = update;
    }

    public Byte getSelect() {
        return select;
    }

    public void setSelect(Byte select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "RoleMenuOperation{" +
                "id=" + id +
                ", roleMenuId=" + roleMenuId +
                ", create=" + create +
                ", delete=" + delete +
                ", update=" + update +
                ", select=" + select +
                '}';
    }
}