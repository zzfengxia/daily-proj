package com.zz.juc.threadrely;

import java.util.concurrent.CountDownLatch;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-06-30 09:42
 * ************************************
 */
public class RelyMain {
    static class RelyThread extends Thread {
        final private CountDownLatch before;
        final private CountDownLatch after;
        public RelyThread(CountDownLatch before, CountDownLatch after) {
            this.before = before;
            this.after = after;
        }

        @Override
        public void run() {
            try {
                if(before != null) {
                    before.await();
                }
                System.out.println("Thread " + Thread.currentThread().getName() + " exec...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (after != null) {
                    after.countDown();
                }
            }
        }
    }

    public static void main(String[] args) {
        CountDownLatch countA = new CountDownLatch(1);
        CountDownLatch countB = new CountDownLatch(1);
        CountDownLatch countC = new CountDownLatch(1);

        Thread a = new RelyThread(countB, null);
        a.setName("A");


        Thread b = new RelyThread(countC, countB);
        b.setName("B");


        Thread c = new RelyThread(null, countC);
        c.setName("C");

        a.start();
        c.start();
        b.start();
    }
}
