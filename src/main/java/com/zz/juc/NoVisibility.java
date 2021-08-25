package com.zz.juc;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-05-13 16:30
 * ************************************
 */
public class NoVisibility {
    private static boolean ready;
    private static int number;
    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new ReaderThread().start();

        }
        number = 42;
        ready = true;
    }
}
