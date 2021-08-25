package com.zz.juc.thisescape;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-04-29 17:40
 * ************************************
 */
public class ThisEscapeTest {
    public static void main(String[] args) {
        EventSource<EventListener> source = new EventSource<EventListener>();
        ListenerRunnable listRun = new ListenerRunnable(source);
        Thread thread = new Thread(listRun);
        thread.start();
        ThisEscape escape1 = ThisEscape.getInstance(source);
    }
}
