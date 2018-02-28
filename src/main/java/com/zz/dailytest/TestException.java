package com.zz.dailytest;

import com.zz.utils.StringUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Francis.zz on 2016-05-25.
 * 描述：<br/>
 */
public class TestException {
    @Test
    public void testException() {
        Map params = new HashMap();
        String data = "hello";
        String str = (String) params.get("name");
        Pattern regex = Pattern.compile("2.1|2.3");
        Matcher matcher = regex.matcher(null);
        boolean result = matcher.matches();
        System.out.println(str);
        System.out.println(StringUtil.isEmpty(str));
        /*try{
            HexUtil.decodeHexStr(data);
        }catch (Exception e) {
            e.printStackTrace();
            data += " Exception!";
            return;
        }*/
        System.out.println(data);
    }
}
