package com.kuliao.oa.domain;

import java.util.List;

public class MenuOperate {
    private Integer menuId;

    private Integer parentId;

    private String icon;

    private String menuName;

    private String index;

    private List<MenuOperate> subs;

    private Byte create;

    private Byte delete;

    private Byte update;

    private Byte select;

    public List<MenuOperate> getSubs() {
        return subs;
    }

    public void setSubs(List<MenuOperate> subs) {
        this.subs = subs;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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
}
