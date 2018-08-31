package com.kuliao.oa.db.model;

public class Passmanage {
    private Integer id;

    private Integer userId;

    private String password;

    private Byte retry;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Byte getRetry() {
        return retry;
    }

    public void setRetry(Byte retry) {
        this.retry = retry;
    }
}