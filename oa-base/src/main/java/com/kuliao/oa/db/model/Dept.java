package com.kuliao.oa.db.model;

import javax.validation.constraints.NotNull;
@NotNull(message = "部门信息不能为空")
public class Dept {

    private Integer deptId;

    private String deptName;

    private Integer deptPId;

    private Integer userId;

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
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public Integer getDeptPId() {
        return deptPId;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", deptPId=" + deptPId +
                ", userId=" + userId +
                '}';
    }

    public void setDeptPId(Integer deptPId) {
        this.deptPId = deptPId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}