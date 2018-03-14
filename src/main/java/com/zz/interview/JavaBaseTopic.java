package com.zz.interview;

import org.junit.Test;

/**
 * Created by Francis.zz on 2018/3/5.
 * java面试-基础
 */
public class JavaBaseTopic {
    /**
     * 值传递还是引用传递的问题
     *
     * 要点：
     * Java 编程语言只有值传递参数。当一个对象实例作为一个参数被传递到方法中时，参数的值就是该对象的引用(对象内存地址值的一个副本)。
     * 所以改变的是这个副本的引用，而引用本身没有改变。
     * 但是对象的内容可以在被调用的方法中改变(只要不创建新的对象)，对象的引用是永远不会改变的。
     */
    @Test
    public void testReference() {
        // 对象内容可以改变，但是对象的引用不能改变
        A a = new A();
        a.name = "a";
        change(a);
        System.out.println(a.name); // 输出“b”，因为方法中引用的副本指向了新的引用。引用本身没有改变，但是引用的对象中的内容变了

        /* ------- 分隔线 -------- */
        String s = new String("ab");
        change(s);
        System.out.println(s);  // 输出“ab”
    }

    /**
     * == 与 equals的区别
     * == 比较的是引用对象的内存地址的值；equals比较的是对象的内容
     *
     * String常量池
     * String与StringBuffer、StringBuilder
     * + 与 StringBuilder
     */
    @Test
    public void testString() {
        String s1 = "ab";
        String s2 = "ab";
        String s3 = new String("ab");
        String s4 = new String(s3);

        System.out.println(s1.equals(s2));                  // true
        System.out.println(s1.equals(s3));                  // true
        System.out.println(s1 == s2);                       // true
        System.out.println(s1 == s3);                       // false
        System.out.println(s3 == s4);                       // false

        String as1 = "abcd";
        String as2 = "ab" + "cd";
        String as3 = "ab" + new String("cd");
        System.out.println(as1 == as2);                     // true
        System.out.println(as1 == as3);                     // false
        /* ------- 分隔线 -------- */
        // Integer 如果超过[-128,127]区间，那么Integer i1 = 130; i1指向的不是常亮本身，而是一个引用
        Integer i1 = 127;
        Integer i2 = 127;
        Integer i3 = new Integer(127);
        System.out.println((i1 == i2));                     // true
        System.out.println((i2 == i3));                     // false

        Integer i4 = 200;
        Integer i5 = 200;
        System.out.println(i4 == i5);                       // false

        /* ------- 分隔线 -------- */
        // 只要字符串拼接中有对象时，就会创建StringBuilder对象，然后用append方法拼接，最后调用StringBuilder的toString方法返回一个重新创建的字符串
        String composeStr = "ai" + s1 + 5 + i1;             // 编译后:(new StringBuilder()).append("ai").append(s1).append(5).append(i1).toString();
    }

    /**
     * String的intern方法分析
     * jdk6及以前的版本会把首次出现的字符串实例复制到 方法区(常量池)，再返回方法区字符串实例的引用；否则直接返回常量池中该字符串的引用
     * jdk7不再复制字符串实例，而是在常量池中记录首次出现的字符串实例的引用，否则返回常量池的引用
     */
    @Test
    public void testStringIntern() {
        String s = new String("1");
        String is1 = s.intern();
        String s2 = "1";
        System.out.println(s == s2);            // false
        System.out.println(is1 == s2);          // true

        String s3 = new String("1") + new String("1");
        String is2 = s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);           // true
        System.out.println(s4 == is2);          // true

        String str2 = "SEUCalvin";//新加的一行代码，其余不变
        String str1 = new String("SEU") + new String("Calvin");
        System.out.println(str1 == str2);       // false
        System.out.println(str1.intern() == str1);  // false
        System.out.println(str1 == "SEUCalvin");    // false
    }

    private void change(A a) {
        a.name = "b";
        a = new A();
        a.name = "c";
    }

    private void change(String s) {
        s = "cd";
    }

    public class A {
        public String name;
    }
}
