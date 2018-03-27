package com.zz.interview;

import org.junit.Test;

/**
 * Created by Francis on 2017/3/15.
 * 描述： <br/>
 */
public class MainTest {
    public static void main(String[] args) {
        // 循环依赖
        //ClassA a = new ClassA(3, 8);


    }

    /**
     * GC标记对象是否存活(两次标记)
     */
    @Test
    public void testGC() throws InterruptedException {
        // finalize自救(垃圾收集时对象重生)
        ClassA.instance = new ClassA();

        ClassA.instance = null;

        // 垃圾收集，在执行ClassA的finalize方法时，重新将对象放到引用链，成功自救
        System.gc();
        // 由于GC线程优先级很低，所以需要等待GC执行
        Thread.sleep(500L);
        if(ClassA.instance != null) {
            ClassA.instance.alived();
        }else {
            System.out.println("I'm dead");
        }

        ClassA.instance = null;
        // 垃圾收集，由于finalize方法只会被执行一次，因此这里无法逃脱被收集的命运
        System.gc();
        Thread.sleep(500L);
        if(ClassA.instance != null) {
            ClassA.instance.alived();
        }else {
            System.out.println("I'm dead");
        }
    }
}
