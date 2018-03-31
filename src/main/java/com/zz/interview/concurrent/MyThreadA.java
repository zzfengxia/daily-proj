package com.zz.interview.concurrent;

/**
 * 实现多线程的方式之一：继承Thread类，由于不能多继承，所有最好还是实现Runnable接口
 * --------------------------------
 * create by Intellij IDEA.
 * @author Francis.zz
 * @date 2018-03-28 15:00
 * --------------------------------
 */
public class MyThreadA extends Thread {
    private HasSelfPrivateNum numRef;

    public MyThreadA(HasSelfPrivateNum numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }
}
