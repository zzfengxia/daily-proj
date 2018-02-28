package com.zz.dailytest;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Francis.zz on 2016-05-18.
 * 描述：<br/>
 */
public class Regex {
    @Test
    public void testRegex() {
        /*String[] data = {"SEscript", "ScrIPtse", "sescriptsd", "sescripsd", "scripsdt"};

        Pattern p = Pattern.compile("script", Pattern.CASE_INSENSITIVE);
        for(String s : data) {
            Matcher m = p.matcher(s);
            System.out.println(m.find());
        }*/

        /** 使用Pattern.CASE_INSENSITIVE参数忽略大小写 */
        String regex = "(15357800D0)|(6715357800)";
        Pattern p2 = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p2.matcher("15357800d0");
        System.out.println("matching result:" + m.matches());
    }
}
