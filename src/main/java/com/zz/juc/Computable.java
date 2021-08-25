package com.zz.juc;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-07-07 16:24
 * ************************************
 */
public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}
