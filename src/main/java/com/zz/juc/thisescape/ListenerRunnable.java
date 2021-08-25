package com.zz.juc.thisescape;

import java.util.List;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-04-29 16:57
 * ************************************
 */
public class ListenerRunnable implements Runnable {

    private EventSource<EventListener> source;
    public ListenerRunnable(EventSource<EventListener> source) {
        this.source = source;
    }
    public void run() {
        List<EventListener> listeners = null;

        try {
            listeners = this.source.retrieveListeners();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for(EventListener listener : listeners) {
            listener.onEvent(new Object());
        }
    }

}
