package com.zz.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * ************************************
 * create by Intellij IDEA
 * 高并发的缓存器实现
 *
 * @author Francis.zz
 * @date 2021-07-07 16:23
 * ************************************
 */
public class Memoizer<A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache
            = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;
    public Memoizer(Computable<A, V> c) { this.c = c; }
    public V compute(final A arg) throws InterruptedException {
        while (true) {
            // 非原子操作，get和后面的set缓存不是原子操作，可能存在多个线程同时get为null的情况，因此需要二次检查
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    public V call() throws InterruptedException {
                        // compute 耗时的计算
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(eval);
                // 缓存计算开始的行为，而不是缓存结果
                f = cache.putIfAbsent(arg, ft);
                // 二次检查，防止重复计算
                if (f == null) { f = ft; ft.run(); }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
            }
        }
    }
}
