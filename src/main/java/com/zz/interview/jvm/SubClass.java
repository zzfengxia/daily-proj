package com.zz.interview.jvm;

/**
 * 子类
 * --------------------------------
 * create by Intellij IDEA.
 * @author Francis.zz
 * @date 2018-03-27 15:37
 * --------------------------------
 */
public class SubClass extends SuperClass {
    // 静态代码块
    static {
        System.out.println("sub static block init...");
    }

    // 普通初始化代码块
    {
        System.out.println("sub block init...");
    }

    public SubClass() {
        System.out.println("sub constructor init...");
    }

    /*@Override
    public void init() {
        System.out.println("sub init method invoke...");
    }*/
}
