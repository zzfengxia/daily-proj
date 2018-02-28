package com.zz.jaxb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXB;
import java.io.*;

/**
 * Created by Francis.zz on 2016/9/12.
 * 描述：
 */
public class TestJaxb {
    private Xuser user;

    @Before
    public void setUp() {
        user = new Xuser();
        user.setUsername("tom");
        user.setNickName("汤姆");
        user.setSex("男");
        Xuser.ContactInfo info = new Xuser.ContactInfo();
        info.setEmail("123@qq.com");
        info.setTellPhone("12345678901");
        user.setContactInfo(info);
    }

    @Test
    public void testJavaToXml() {
        Writer writer = new StringWriter();
        JAXB.marshal(user, writer);
        System.out.println(writer.toString());
    }

    @Test
    public void testXmlToJava() {
        String xml = new String("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<user sex=\"男\">\n" +
                "    <username>tom</username>\n" +
                "    <nickName>汤姆</nickName>\n" +
                "    <contactInfo email=\"123@qq.com\" phone=\"12345678901\"/>\n" +
                "</user>");
        Xuser u = null;
        u = JAXB.unmarshal(new StringReader(xml), Xuser.class);
        Assert.assertNotNull(u);

    }

}
