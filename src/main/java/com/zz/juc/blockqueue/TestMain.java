package com.zz.juc.blockqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-07-01 17:19
 * ************************************
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingDeque = new ArrayBlockingQueue<String>(5);

        for (int i = 0; i < 10; i++) {
            new Thread(new Producer(blockingDeque)).start();
        }

        for (int i = 0; i < 5; i++) {
            new Thread(new Consumer(blockingDeque)).start();
        }

        Thread.sleep(2000);
        System.out.println(blockingDeque.size());
    }
}
