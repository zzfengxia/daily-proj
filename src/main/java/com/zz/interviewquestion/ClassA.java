package com.zz.interviewquestion;

/**
 * Created by Francis on 2017/3/15.
 * 描述： <br/>
 */
public class ClassA {
    int a = 1;
    int b;
    ClassB cb = new ClassB(2, 6);

    public ClassA(int a, int b) {
        this.a = a;
        this.b = b;
    }
}
