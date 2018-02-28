package com.zz.jaxb;


import javax.xml.bind.annotation.*;

/**
 * Created by Francis.zz on 2016/9/12.
 * 描述：jaxb使用
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class Xuser {
    @XmlElement
    private String username;
    @XmlElement
    private String nickName;
    @XmlElement
    private ContactInfo contactInfo;
    @XmlAttribute(name = "sex")
    private String sex;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ContactInfo {
        @XmlAttribute(name = "email")
        private String email;
        @XmlAttribute(name = "phone")
        private String tellPhone;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTellPhone() {
            return tellPhone;
        }

        public void setTellPhone(String tellPhone) {
            this.tellPhone = tellPhone;
        }
    }
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

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
