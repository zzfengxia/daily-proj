package com.zz.bean;

/**
 * Created by Francis.zz on 2016-05-26.
 * 描述：<br/>
 */
public class Person extends Enimals{
    private String username;            // 用户名
    private String contactPhone;        // 联系方式
    private String email;               // 邮箱
    private String hight;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return "female";
    }

    public boolean isRight() {
        return username.equals("Francis");
    }
}
