package com.zz.interview.jvm;

/**
 * 父类
 * --------------------------------
 * create by Intellij IDEA.
 * @author Francis.zz
 * @date 2018-03-27 15:37
 * --------------------------------
 */
public class SuperClass {
    // 静态代码块
    static {
        System.out.println("super static block init...");
    }

    // 普通初始化代码块
    {
        System.out.println("super block init...");
    }

    public SuperClass() {
        System.out.println("super constructor init...");
    }

    public void init() {
        System.out.println("super init method invoke...");
    }
}
