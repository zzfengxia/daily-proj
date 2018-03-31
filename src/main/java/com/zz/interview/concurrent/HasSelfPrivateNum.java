package com.zz.interview.concurrent;

/**
 * --------------------------------
 * create by Intellij IDEA.
 *
 * @author Francis.zz
 * @date 2018-03-28 16:29
 * --------------------------------
 */
public class HasSelfPrivateNum {
    private int num = 0;
    private String name;

    public HasSelfPrivateNum() {

    }

    public HasSelfPrivateNum(String name) {
        this.name = name;
    }

    public void print() {
        for (int i = 1; i <= 100; i++) {
            System.out.println(name + ":" + i);
        }
    }

    public synchronized static void show() throws InterruptedException {
        System.out.println("进入静态方法");
        Thread.sleep(1000);
        System.out.println("退出静态方法");
    }

    public synchronized void desc() throws InterruptedException {
        System.out.println("进入实例方法");
        Thread.sleep(1000);
        System.out.println("退出实例方法");
    }

    /**
     * wait方法会使线程阻塞，并释放锁
     */
    public synchronized void mWaite() throws InterruptedException {
        System.out.println("开始等待...");
        this.wait();
        System.out.println("被唤醒...");
    }

    public synchronized void mNotify() throws InterruptedException {
        Thread.sleep(2000);
        this.notifyAll();
        System.out.println("准备唤醒...");
    }

    public synchronized void addI(String username) {
        try {
            if (username.equals("a")) {
                num = 100;
                System.out.println("a set over!");
                Thread.sleep(2000);
            } else {
                num = 200;
                System.out.println("b set over!");
            }
            System.out.println(username + " num=" + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
