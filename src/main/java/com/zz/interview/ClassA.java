package com.zz.interview;

/**
 * Created by Francis on 2017/3/15.
 * 描述： <br/>
 */
public class ClassA {
    public static ClassA instance = null;
    int a = 1;
    int b;
    //ClassB cb = new ClassB(2, 6);

    public ClassA(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public ClassA() {

    }

    public void alived() {
        System.out.println("i'm still alive");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        instance = this;
        System.out.println("finalize has been invoked");
    }
}
