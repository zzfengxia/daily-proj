package com.zz.juc.thisescape;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-04-29 17:39
 * ************************************
 */
public class ThisEscape {
    public final int id;
    public final String name;
    private final EventListener eventListener;

    /*public ThisEscape(EventSource<EventListener> source) {
        id = 1;
        // ThisEscape还未声明完成，就由内部类将ThisEscape的this引用传递发布出去了
        source.registerListener(new EventListener() {
            public void onEvent(Object obj) {
                // this引用逃逸，可能导致name还未初始化就被访问了
                System.out.println("id: " + ThisEscape.this.id);
                System.out.println("name: " + ThisEscape.this.name);
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        name = "Tom";
    }*/

    /**
     * 防止this引用逃逸
     */
    private ThisEscape() {
        id = 1;
        eventListener = new EventListener() {
            public void onEvent(Object obj) {
                // this引用逃逸，可能导致name还未初始化就被访问了
                System.out.println("id: " + ThisEscape.this.id);
                System.out.println("name: " + ThisEscape.this.name);
            }
        };

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        name = "Tom";
    }

    public static ThisEscape getInstance(EventSource<EventListener> source) {
        ThisEscape thisEscape = new ThisEscape();
        // 先创建完ThisEscape对象，再发布出去
        source.registerListener(thisEscape.eventListener);

        return thisEscape;
    }
}
