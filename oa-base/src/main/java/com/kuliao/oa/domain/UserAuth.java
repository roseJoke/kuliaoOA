package com.kuliao.oa.domain;

public class UserAuth{
    private Integer userId;
    private String empNo;
    private Byte isBlock;
    private String username;
    private String password;
    private Byte retry;


    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    private String lastLogin;

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

    public Byte getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(Byte isBlock) {
        this.isBlock = isBlock;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getRetry() {
        return retry;
    }

    public void setRetry(Byte retry) {
        this.retry = retry;
    }


    @Override
    public String toString() {
        return "UserAuth{" +
                "userId=" + userId +
                ", empNo='" + empNo + '\'' +
                ", isBlock=" + isBlock +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", retry=" + retry +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
