package com.kuliao.oa.db.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class Customer {
    private Integer id;
    @NotBlank(message = "客户姓名不能为空")
    private String cname;
    @NotBlank(message = "工作单位不能为空")
    private String cwork;
    @Pattern(regexp = "^[0][1-9]{2,3}-[0-9]{5,10}$", message = "电话号码格式不正确，缺少\"-\"号")
    private String cphone;
    @Email(message = "邮箱格式不正确")
    private String cemail;
    @NotBlank(message = "工作沟通不能为空")
    private String conmunacation;
    private String beizhu;
    @NotBlank(message = "客户类型不能为空")
    private String ctype;

    public Customer() {
    }

    public Customer(Integer id, String cname, String cwork, String cphone, String cemail, String conmunacation, String beizhu, String ctype) {
        this.id = id;
        this.cname = cname;
        this.cwork = cwork;
        this.cphone = cphone;
        this.cemail = cemail;
        this.conmunacation = conmunacation;
        this.beizhu = beizhu;
        this.ctype = ctype;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getCwork() {
        return cwork;
    }

    public void setCwork(String cwork) {
        this.cwork = cwork == null ? null : cwork.trim();
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone == null ? null : cphone.trim();
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail == null ? null : cemail.trim();
    }

    public String getConmunacation() {
        return conmunacation;
    }

    public void setConmunacation(String conmunacation) {
        this.conmunacation = conmunacation == null ? null : conmunacation.trim();
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu == null ? null : beizhu.trim();
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype == null ? null : ctype.trim();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", cname='" + cname + '\'' +
                ", cwork='" + cwork + '\'' +
                ", cphone='" + cphone + '\'' +
                ", cemail='" + cemail + '\'' +
                ", conmunacation='" + conmunacation + '\'' +
                ", beizhu='" + beizhu + '\'' +
                ", ctype='" + ctype + '\'' +
                '}';
    }
}