package com.kuliao.oa.db.model;

public class Menu {
    private Integer menuId;

    private String icon;

    private String menuName;

    private Integer parentId;

    private String index;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuId=" + menuId +
                ", icon='" + icon + '\'' +
                ", menuName='" + menuName + '\'' +
                ", parentId=" + parentId +
                ", index='" + index + '\'' +
                '}';
    }

    public void setIndex(String index) {
        this.index = index == null ? null : index.trim();
    }
}