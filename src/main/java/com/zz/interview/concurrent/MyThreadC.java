package com.zz.interview.concurrent;

/**
 * --------------------------------
 * create by Intellij IDEA.
 *
 * @author Francis.zz
 * @date 2018-03-28 16:48
 * --------------------------------
 */
public class MyThreadC extends Thread {
    @Override
    public void run() {
        super.run();
        try {
            HasSelfPrivateNum.show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
