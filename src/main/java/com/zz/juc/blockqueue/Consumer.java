package com.zz.juc.blockqueue;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-07-01 17:18
 * ************************************
 */
public class Consumer implements Runnable {
    private BlockingQueue<String> blockingDeque;

    public Consumer() {
    }

    public Consumer(BlockingQueue<String> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }

    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    public static void main(String[] args) {
        int c = new Consumer().ctl.get();
        int rs = runStateOf(c);
        System.out.println(rs);
        System.out.println(RUNNING);
    }

    @Override
    public void run() {
        try {
            String msg = blockingDeque.take();
            System.out.println(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
