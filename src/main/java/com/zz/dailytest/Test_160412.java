package com.zz.dailytest;

import com.zz.bean.Person;
import com.zz.bean.User;
import com.zz.utils.HexUtil;
import com.zz.utils.StringUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zz on 2016-04-12.
 * 描述：<br/>
 */
public class Test_160412 {
    @Test
    public void testParseBalance() {
        String data = "zz";
        String[] tempData = data.split(" ");
        data = StringUtil.arrayToString(tempData, "|");
        System.out.println(data);
        String resHex = "00 00 00 64 90 00";
        try {
            float balance = byteArrayToInt(HexUtil.hexToByteArray(resHex), 0) / 100;
            System.out.println("Balance：" + balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 解析空充后的余额APDU
    public static int byteArrayToInt(byte[] bytes,int offset) {
        int result = 0;
        result = result | (0xFF000000 & (bytes[offset+0] << 24));
        result = result | (0x00FF0000 & (bytes[offset+1] << 16));
        result = result | (0x0000FF00 & (bytes[offset+2] << 8));
        result = result | (0x000000FF & (bytes[offset+3]));
        return result;
    }

    // 测试使用参数传递返回结果
    @Test
    public void testParams() throws Exception {
        User user = new User();
        getTotal(user);
        System.out.println("---------real---------");
        for(Field f : user.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            System.out.println(f.getName() + ":" + f.get(user));
        }
        /*User u2 = new User();
        u2 = controller;
        for(Field f : u2.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            System.out.println(f.getName() + ":" + f.get(u2));
        }*/
    }

    // 测试合并对象属性
    private static void getTotal(User u) throws Exception {
        Person person = new Person();
        person.setEmail("128");
        StringUtil.copyBean(person, u);


        System.out.println("---------getTotal---------");
        for(Field f : u.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            System.out.println(f.getName() + ":" + f.get(u));
        }
    }

    @Test
    public void testException() {
        try {
            throw new Exception("test Exception");
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-- 2");
    }

    // 测试System.out.printf
    @Test
    public void testPrint() {
        String name = "Francis";
        int age = 24;
        System.out.printf("My name is %2$-10s, age is %1$4d.\n", age, name);
    }

    // 测试日期与字符串转换
    @Test
    public void testDateFormat() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentTimeStr = format.format(new Date());
        System.out.println("string:" + currentTimeStr);

        try {
            Date currentTimeDate = format.parse(currentTimeStr);
            System.out.println("Date:" + currentTimeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
