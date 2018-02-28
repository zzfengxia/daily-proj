package com.zz.bean;

import java.util.Date;

/**
 * Created by Francis.zz on 2016-04-13.
 * 描述：<br/>
 */
public class User extends Enimals {
    private String username;            // 用户名
    private String nickName;            // 昵称
    private String pwd;                 // 密码
    private String contactPhone;        // 联系方式
    private String email;               // 邮箱
    private Date birthDay;              // 出生日期
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}
