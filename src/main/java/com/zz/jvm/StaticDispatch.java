package com.zz.jvm;

import java.io.Serializable;
import java.util.Random;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-04-20 11:33
 * ************************************
 */
public class StaticDispatch {
    static abstract class Human {
    }
    static class Man extends Human {
    }
    static class Woman extends Human {
    }
    public synchronized void sayHello(Human guy) {
        System.out.println("hello,guy!");
    }
    public void sayHello(Man guy) {
        System.out.println("hello,gentleman!");
    }
    public void sayHello(Woman guy) {
        System.out.println("hello,lady!");
    }

    public static void sayHello(Object arg) {
        System.out.println("hello Object");
    }
    /*public static void sayHello(int arg) {
        System.out.println("hello int");
    }*/
    public static void sayHello(long arg) {
        System.out.println("hello long");
    }
    public static void sayHello(Character arg) {
        System.out.println("hello Character");
    }
    /*public static void sayHello(char arg) {
        System.out.println("hello char");
    }*/
    public static void sayHello(char... arg) {
        System.out.println("hello char ...");
    }
    public static void sayHello(Serializable arg) {
        System.out.println("hello Serializable");
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        StaticDispatch sr = new StaticDispatch();
        sr.sayHello(man); // hello,guy!
        sr.sayHello(woman); // hello,guy!

        Human human = (new Random()).nextBoolean() ? new Man() : new Woman();

        int[][][] a = new int[1][1][1];
        // char>int>long>float>double
        StaticDispatch.sayHello('a');
    }
}
