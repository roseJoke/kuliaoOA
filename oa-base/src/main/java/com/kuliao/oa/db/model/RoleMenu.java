package com.kuliao.oa.db.model;

public class RoleMenu {
    private Integer id;

    private Integer roleId;

    private Integer menuId;

    public RoleMenu() {
    }

    public RoleMenu(Integer roleId, Integer menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "RoleMenu{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", menuId=" + menuId +
                '}';
    }
}