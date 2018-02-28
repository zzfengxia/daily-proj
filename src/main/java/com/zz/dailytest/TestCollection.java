package com.zz.dailytest;


import org.junit.Test;

import java.util.*;

/**
 * Created by Francis on 2017/3/10.
 * 描述： <br/>
 */
public class TestCollection {
    @Test
    public void testCollection() {
        discMsg("Z");
        discMsg(5);

        // LinkedList
        List<String> strs = new LinkedList<String>();
        // 测试ArrayList和LinkedList的区别

        Integer i1 = 100;
        Integer i2 = 100;

        Integer i3 = -128;
        Integer i4 = -128;
        System.out.println(i1 == (i2)); // true
        System.out.println(i3 == (i4)); // false 超过[-128,127]区间

        String a = "ss";
        String b = new String("ss");
        String c = new String(b);
        String d = "ss";

        System.out.println("a:" + System.identityHashCode(a));  // a:1558395735
        System.out.println("b:" + System.identityHashCode(b));  // b:1246559333
        System.out.println("c:" + System.identityHashCode(c));  // c:72249599
        System.out.println("d:" + System.identityHashCode(d));  // d:1558395735

        System.out.println(a == b); // false
        System.out.println(c.equals(b)); // true 比较的是引用指向的常亮值
        System.out.println(c == b); // false 比较的时b和c本身的值（引用变量的地址不同）

        String s1 = "hello";
        String s2 = "hello";
        System.out.println(s1 == s2);  // true

    }

    private <E> void discMsg(E ele) {
        System.out.println("Hello, " + ele);
    }

    public static void main(String[] args) {
        Set<String> words = new HashSet<>();
        long totalTime = 0;
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String word = scanner.next();
            if("francis".equals(word)) {
                break;
            }
            Long callTime = System.currentTimeMillis();
            words.add(word);
            callTime = System.currentTimeMillis() - callTime;
            totalTime += callTime;
        }

        Iterator<String> it = words.iterator();
        for(int i = 1; i <= 20 && it.hasNext(); i++) {
            System.out.println(it.next());
            System.out.println("...");
            System.out.println(words.size() + "distinct words." + totalTime);
        }
    }

    @Test
    public void testHashSet() {
        List<String> originalStr = new ArrayList<>();
        originalStr.add("1");
        originalStr.add("2");

        // 使用Collectios中的方法获取不可修改的集合视图，但是可以通过修改原集合来达到修改视图的目的；
        List<String> unmodifyStr = Collections.unmodifiableList(originalStr);
        try{
            System.out.println(unmodifyStr.toString());
            unmodifyStr.add("3");
            System.out.println(unmodifyStr.toString());
        }catch (Exception e){
            System.out.println("error. unsupported!");
            originalStr.add("3");
            System.out.println(unmodifyStr.toString());
        }

        // 集合转换为数组
        String[] array = originalStr.toArray(new String[originalStr.size()]);
        for(String s : array) {
            System.out.println(s);
        }
    }

}
