package com.zz.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-04-23 16:25
 * ************************************
 */
public class TestReturnFinally {
    public static List<String> func() {
        List<String> i = new ArrayList<>();
        try{
            i.add("1");
            return i;
        } finally {
            i.add("2");
            System.out.println(i);
        }
    }

    public static int func2() {
        int i = 0;
        try{
            i = 10;
            return ++i;
        } finally {
            i = i + 2;
            System.out.println("finally:" + i);
        }
    }

    public static void main(String[] args) {
        System.out.println(func());
        System.out.println(func2());
    }
}
