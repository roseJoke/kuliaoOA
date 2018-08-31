package com.kuliao.oa.domain;

import javax.validation.constraints.NotNull;

@NotNull
public class UserInfo {
    private Integer userId;
    private String code;
    private String empNo;
    private String username;
    private String deptName;

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", code='" + code + '\'' +
                ", empNo='" + empNo + '\'' +
                ", username='" + username + '\'' +
                ", deptName='" + deptName + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


}
