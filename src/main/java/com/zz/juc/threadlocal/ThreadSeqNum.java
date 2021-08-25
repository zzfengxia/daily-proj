package com.zz.juc.threadlocal;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-05-17 15:50
 * ************************************
 */
public class ThreadSeqNum {
    private static ThreadLocal<Integer> sequence = ThreadLocal.withInitial(() -> 0);

    public static Integer getSequence() {
        sequence.set(sequence.get() + 1);
        return sequence.get();
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            final int t = i;
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println("Thread:" + t + " seqNum:" + getSequence());
                }
            }).start();
        }

        Thread.sleep(500);
    }
}
