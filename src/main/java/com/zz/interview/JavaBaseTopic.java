package com.zz.interview;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

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

        Integer a = new Integer(3);
        Integer b = 3;                  // 将3自动装箱成Integer类型
        int c = 3;
        System.out.println(a == b);     // false 两个引用没有引用同一对象
        System.out.println(a == c);     // true a自动拆箱成int类型再和c比较

        /* ------- 分隔线 -------- */
        // 只要字符串拼接中有对象时，就会创建StringBuilder对象，然后用append方法拼接，最后调用StringBuilder的toString方法返回一个重新创建的字符串
        String composeStr = "ai" + s1 + 5 + i1;             // 编译后:(new StringBuilder()).append("ai").append(s1).append(5).append(i1).toString();
    }

    /**
     * String的intern方法分析
     * jdk6及以前的版本会把首次出现的字符串实例复制到 方法区(常量池)，再返回方法区字符串实例的引用；否则直接返回常量池中该字符串的引用
     * jdk7不再复制字符串实例，而是在常量池中记录首次出现的字符串实例的引用，否则返回常量池的引用
     *
     * new String, new Integer创建了几个对象的问题，以及的问题；可以使用debug查看
     */
    @Test
    public void testStringIntern() {
        // 一下输出都是基于java7环境
        String s = new String("1");         // 创建了两个对象，heap和常量池;s为heap的引用
        String is1 = s.intern();            // 常量池中已有“1”，返回常量池的引用
        String s2 = "1";                    // 常量池中已有“1”，返回常量池的引用
        System.out.println(s == s2);        // false(s是heap上的引用，s2是常量池的引用)
        System.out.println(is1 == s2);      // true

        String s3 = new String("1") + new String("1");  // s3为heap上的引用(这里会优化为StringBuilder，所以不会再常量次中创建)
        String is2 = s3.intern();           // 常量中没有，所以会返回heap上实例的引用(常量池中记录heap实例的引用)
        String s4 = "11";                   // 常量池有“11”，但实际是上面记录的heap上的引用，所以这里也是指向的heap上的引用
        System.out.println(s3 == s4);       // true
        System.out.println(s4 == is2);      // true

        // 2 start
        String str2 = "SEUCalvin";                                  // 常量池的引用
        String str1 = new String("SEU") + new String("Calvin");     // 创建了5个对象，str1为heap上的引用
        System.out.println(str1 == str2);                           // false
        System.out.println(str1.intern() == str1);                  // false 由于常量池已存在“SEUCalvin”字符串，所以str1.intern()返回常量池的引用
        System.out.println(str1 == "SEUCalvin");                    // false
        // 2 end

        String o1 = new String("C") + "alvin";                    // 这样合并的形式不会在常量池中创建“Calvin”实例
        System.out.println(o1.intern() == o1);                    // 注释掉“2”的代码，此处返回true;打开后打印false，因为代码块2 中已经在常量池创建了“Calvin”实例

        String sb1 = new StringBuilder("Are").toString();         // 这种形式也会在常量池创建“Are”，因为“Are”本身就是String常量
        System.out.println(sb1.intern() == sb1);                  // false
    }

    /**
     * 创建了几个对象的问题
     * new String + new String会在编译器使用StringBuilder优化
     */
    @Test
    public void howMuchObjects() {
        //String s1 = "a" + "b";                     // 1个,"ab"在常量池
        //String s2 = new String("abc");    // 2个,heap和常量池
        String s3 = new String("ab");     // 1个,前面已经在常量池中创建了,所有只在heap上
        String s4 = new String("12") + "34";    // 4个；常量池两个，heap上有“12” “1234”

        //Integer i1 = 1;
        Integer i2 = new Integer(130);
        //System.out.println(s4);
    }

    /**
     * 反转字符串
     */
    @Test
    public void testReverseString() {
        System.out.println(reverse("Hello"));
    }

    @Test
    public void base() {
        // float f = 3.4;   // 错误，3.4是double类型，向下转型需要强转
        float f = 3.4F;
        System.out.println(f);

        short st1 = 1;
        //st1 = st1 + 1;   // 错误写法，1是int类型，向下转型需要强转
        st1 += 1;           // 这种写法隐含了强转

        System.out.println(Math.round(-1.6)); // 加0.5向下取整
    }

    /**
     * 使用java7的NIO和传统递归的方式遍历文件
     * 文件夹数量大时，NIO效率较优
     */
    @Test
    public void testTraversalFile() throws IOException {
        long start = System.currentTimeMillis();
        String path = "E:\\work\\一卡通对接\\吉林一卡通\\生产卡数据\\20180323";
        // NIO
        traversal(path);
        long end = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        File file = new File(path);
        // 递归
        traversal(file);
        long end2 = System.currentTimeMillis();

        System.out.println("NIO耗时：" + (end - start));
        System.out.println("递归耗时：" + (end2 - start2));
    }

    /**
     * 递归方法反转字符串
     */
    private String reverse(String originStr) {
        if(originStr == null || originStr.length() <= 1)
            return originStr;
        return reverse(originStr.substring(1)) + originStr.charAt(0);
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

    /**
     * 使用java7的nio遍历文件
     *
     * @param path
     * @throws IOException
     */
    public void traversal(String path) throws IOException {
        Path initPath = Paths.get(path);
        Files.walkFileTree(initPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file.getFileName().toString());
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 传统递归方法遍历文件
     *
     * @param file
     * @throws Exception
     */
    public void traversal(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for(File f : files) {
                traversal(f);
            }
        }else {
            // do something
            System.out.println(file.getName());
        }
    }
}
