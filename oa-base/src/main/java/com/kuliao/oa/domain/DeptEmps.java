package com.kuliao.oa.domain;

import com.kuliao.oa.db.model.Dept;

import java.util.List;

public class DeptEmps {
    private Integer deptId;
    private String deptName;
    private List<Dept> deptEmps;

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

    public List<Dept> getDeptEmps() {
        return deptEmps;
    }

    public void setDeptEmps(List<Dept> deptEmps) {
        this.deptEmps = deptEmps;
    }

    @Override
    public String toString() {
        return "DeptEmps{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", deptEmps=" + deptEmps +
                '}';
    }
}


