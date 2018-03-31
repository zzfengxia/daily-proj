package com.zz.interview.concurrent;

import java.util.concurrent.Callable;

/**
 * 实现多线程的方式之一：实现Callable接口
 * 相比Runnable接口，该接口允许返回计算结果
 * --------------------------------
 * create by Intellij IDEA.
 * @author Francis.zz
 * @date 2018-03-28 14:56
 * --------------------------------
 */
public class MyCallable implements Callable<Integer> {
    private int factor;

    public MyCallable(int i) {
        this.factor = i;
    }

    @Override
    public Integer call() throws Exception {
        // 计算一个数累加的结果
        Integer res = 0;
        int i = factor;
        while(i > 0) {
            res += i--;
        }
        System.out.println("开始计算:" + factor + "\t结果为:" + res);
        return res;
    }
}
