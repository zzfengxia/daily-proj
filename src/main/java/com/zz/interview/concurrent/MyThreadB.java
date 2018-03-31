package com.zz.interview.concurrent;

/**
 * --------------------------------
 * create by Intellij IDEA.
 *
 * @author Francis.zz
 * @date 2018-03-28 16:29
 * --------------------------------
 */
public class MyThreadB extends Thread {
    private HasSelfPrivateNum numRef;

    public MyThreadB(HasSelfPrivateNum numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        try {
            numRef.desc();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
