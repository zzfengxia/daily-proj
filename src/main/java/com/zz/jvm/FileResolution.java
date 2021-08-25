package com.zz.jvm;

/**
 * ************************************
 * create by Intellij IDEA
 * 类加载的<b>解析</b>阶段
 *
 * @author Francis.zz
 * @date 2021-04-15 10:36
 * ************************************
 */
public class FileResolution {
    interface Interface0 {
        int A = 0;
    }
    interface Interface1 extends Interface0 {
        int A = 1;
    }
    interface Interface2 {
        int A = 2;
    }
    static class Parent implements Interface1 {
        //public static int A = 3;
    }
    static class Sub extends Parent implements Interface2 {
        //public static int A = 4;
    }

    static {
        i = 10;
    }

    static int i = 1;

    public static void main(String[] args) {
        System.out.println(i);
    }
}
