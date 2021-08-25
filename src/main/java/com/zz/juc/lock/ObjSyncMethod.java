package com.zz.juc.lock;

/**
 * ************************************
 * create by Intellij IDEA
 * synchronized修饰实例方法获取对象锁
 *
 * @author Francis.zz
 * @date 2021-05-12 17:09
 * ************************************
 */
public class ObjSyncMethod {
    public synchronized void say(String invoker) {
        System.out.println(invoker + " say something...");
        try {
            // 这里展示了synchronized的可重入性
            say2(invoker);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void say2(String invoker) {
        System.out.println(invoker + " say2 invoke");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * synchronized修饰实例方法，锁定对象，需要拿到this锁，同一个对象的多个同步实例方法的锁资源都是调用者实例
     * 锁是针对多个线程的
     */
    public synchronized void walk(String invoker) {
        System.out.println(invoker + " walk a step...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ObjSyncMethod obj = new ObjSyncMethod();

        // 多个线程会竞争同一对象的不同同步实例方法的实例锁
        new Thread(() -> {
            obj.say("t1");
        }).start();

        new Thread(() -> {
            obj.walk("t2");
        }).start();
    }
}
