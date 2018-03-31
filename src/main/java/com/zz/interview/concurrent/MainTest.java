package com.zz.interview.concurrent;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * --------------------------------
 * create by Intellij IDEA.
 * @author Francis.zz
 * @date 2018-03-28 15:24
 * --------------------------------
 */
public class MainTest {

    @Test
    public void testCallable() throws ExecutionException, InterruptedException {
        List<Future<Integer>> futures = Lists.newArrayListWithCapacity(10);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            // 实现Callable接口的类必须使用ExecutorService.submit调用
            Future<Integer> f = executor.submit(new MyCallable((int) (Math.random() * 100)));
            futures.add(f);
        }
        Integer sum = 0;
        for(Future<Integer> f : futures) {
            sum += f.get();
        }

        System.out.println(sum);
    }

    /**
     * volatile关键字可以保证可见性和有序性，但无法保证原子性，使用AtomicInteger可保证原子性
     */
    private volatile AtomicInteger i = new AtomicInteger(0);

    public void increaseI() {
        // 非原子操作
        i.getAndIncrement();
    }

    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();    //注意这个地方

    /**
     * 类锁和对象锁互不干预
     * 类锁实际是Class对象锁
     */
    public static void main(String[] args) throws InterruptedException {
        /*HasSelfPrivateNum numRef1 = new HasSelfPrivateNum();
        HasSelfPrivateNum numRef2 = new HasSelfPrivateNum();

        MyThreadA athread = new MyThreadA(numRef1);
        athread.start();

        MyThreadB bthread = new MyThreadB(numRef2);
        bthread.start();

        MyThreadC cthread = new MyThreadC();
        cthread.run();*/

        // -------------------------------------
        /*final MainTest test = new MainTest();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        test.increaseI();
                    }
                }
            }).start();
        }

        // 保证前面的线程已经执行结束
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println(Thread.activeCount());
        System.out.println(test.i.get());*/


        // -------------------------------------

        /**
         * thread.join方法阻塞主进程，等待thread执行完毕
         */
        // join()方法
        /*Thread th1 = new Thread(new Runnable() {
            @Override
            public void run() {
                HasSelfPrivateNum obj = new HasSelfPrivateNum("Tom");
                obj.print();
            }
        });

        Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                HasSelfPrivateNum obj = new HasSelfPrivateNum("Jerry");
                obj.print();
            }
        });

        // 进入就绪态
        th1.start();
        th1.join();

        th2.start();*/


        // -------------------------------------
        // wait()方法 wait方法会使线程阻塞，并释放锁
        /*final HasSelfPrivateNum obj6 = new HasSelfPrivateNum();
        Thread th1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    obj6.mWaite();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    obj6.mNotify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th2.start();
        th1.start();*/

        // -------------------------------------
        // Lock，ReentrantLock
        final MainTest test = new MainTest();

        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            }
        }.start();

        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            }
        }.start();
    }

    private void insert(Thread thread) {
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i=0;i<5;i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            System.out.println(thread.getName()+"释放了锁");
            lock.unlock();
        }
    }
}
