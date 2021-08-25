package com.zz.juc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * ************************************
 * create by Intellij IDEA
 * 使用 Semaphore 信号量实现有界容器，为容器添加边界
 *
 * @author Francis.zz
 * @date 2021-07-07 15:18
 * ************************************
 */
public class BoundedHashSet<T> {
    private final Set<T> ele;
    private final Semaphore semaphore;

    public BoundedHashSet(int bound) {
        ele = Collections.synchronizedSet(new HashSet<>());
        semaphore = new Semaphore(bound);
    }

    public boolean add(T data) throws InterruptedException {
        semaphore.acquire();
        boolean isAdded = false;
        try {
            isAdded = ele.add(data);
            return isAdded;
        } finally {
            if (!isAdded) {
                semaphore.release();
            }
        }
    }

    public boolean remove(T data) {
        boolean isRemoved = ele.remove(data);
        if(isRemoved) {
            semaphore.release();
        }
        return isRemoved;
    }
    public int size() {
        return ele.size();
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedHashSet<Integer> set = new BoundedHashSet<>(3);
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    set.add(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(100);
        System.out.println(set.size());
    }
}
