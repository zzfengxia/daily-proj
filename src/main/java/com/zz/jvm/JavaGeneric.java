package com.zz.jvm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-04-27 15:08
 * ************************************
 */
public class JavaGeneric {
    /*public static void type(List<String> param) {

    }

    public static void type(List<Integer> param) {

    }

    // 下面这两个方法在jdk6的javac可以编译
    public static String method(List<String> list) {
        System.out.println("invoke method(List<String> list)");
        return "";
    }
    public static int method(List<Integer> list) {
        System.out.println("invoke method(List<Integer> list)");
        return 1;
    }*/

    public static void main(String[] args) {
        // 编译后类型会被擦除
        Map<String, String> map = new HashMap<String, String>();
        map.put("hello", "你好");
        // 编译后调用的map.put(Object, Object)方法
        map.put("how are you?", "吃了没？ ");
        // 编译后元素访问时会加入类型检查指令
        System.out.println(map.get("hello"));
        System.out.println(map.get("how are you?"));

        autoBoxing2();
    }

    public static void autoBoxing() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        int sum = 0;
        for (int i : list) {
            sum += i;
        }
        System.out.println(sum);
    }

    public static void autoBoxing2() {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d); 
        System.out.println(e == f); 
        System.out.println(c == (a + b)); 
        System.out.println(c.equals(a + b)); 
        System.out.println(g == (a + b)); 
        System.out.println(g.equals(a + b)); 
    }
}
