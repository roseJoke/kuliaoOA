package com.kuliao.oa.domain;

import com.kuliao.oa.db.model.Dept;

import java.util.List;

public class MyDept {
    private Integer deptId;
    private String deptName;
    private Integer userId;
    private List<Dept> subs;
    private Integer deptPId;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Dept> getSubs() {
        return subs;
    }

    public void setSubs(List<Dept> subs) {
        this.subs = subs;
    }

    public Integer getDeptPId() {
        return deptPId;
    }

    public void setDeptPId(Integer deptPId) {
        this.deptPId = deptPId;
    }

    @Override
    public String toString() {
        return "MyDept{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", userId=" + userId +
                ", subs=" + subs +
                ", deptPId=" + deptPId +
                '}';
    }
}
