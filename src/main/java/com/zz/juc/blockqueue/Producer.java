package com.zz.juc.blockqueue;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-07-01 17:17
 * ************************************
 */
public class Producer implements Runnable {
    private final BlockingQueue<String> blockingDeque;

    public Producer(BlockingQueue<String> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }


    @Override
    public void run() {
        try {
            String msg = RandomStringUtils.randomNumeric(5);
            System.out.println("msg before:" + msg);
            blockingDeque.put(msg);
            System.out.println("msg after:" + msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
