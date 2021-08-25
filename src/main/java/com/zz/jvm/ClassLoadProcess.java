package com.zz.jvm;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-05-10 15:09
 * ************************************
 */
public class ClassLoadProcess {
    // 静态变量
    public static int age = 28;
    // 常量
    public static final int money = 100;
    // 实例变量
    public String name = "Tom";

    public ClassLoadProcess() {
        showMsg();
    }

    // 静态代码块
    static {
        age = age + 1;
    }

    // 代码块
    {
        name = name.toUpperCase();
    }

    public void showMsg() {
        //System.out.println("name:" + name);
    }
}
