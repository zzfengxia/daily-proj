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
     * Java 编程语言只有值传递参数。当一个对象实例作为一个参数被传递到方法中时，参数的值就是该对象的引用(一个副本，地址的值)。
     * 指向同一个对象,对象的内容可以在被调用的方法中改变，但对象的引用是永远不会改变的。
     */
    @Test
    public void testReference() {
        A a = new A();
        a.name = "a";
        change(a);
        System.out.println(a.name); // 仍然输出“a”
    }

    private void change(A a) {
        a = new A();
        a.name = "b";
    }

    public class A {
        public String name;
    }
}
