package com.zz.juc;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-07-01 14:31
 * ************************************
 */
public class JUCTest {
    /**
     * 多线程下HashMap put后再get会死循环，并不会一定重现
     */
    private static void hashMapLoop() throws InterruptedException {
        final HashMap<String, String> map = new HashMap<String, String>(2);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    final int key = i * 2 + 1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(key +"", "12");
                        }
                    }, "ftf" + i).start();
                }
            }
        }, "ftf");
        t.start();
        t.join();
        System.out.println(map.size());
        System.out.println("value:" + map.get("1"));
    }

    /**
     * 线程池
     */
    private static void threadPool() {
        // 阻塞队列参数可以设置允许存放的任务数量，ArrayBlockingQueue 超过数据在提交任务会报错
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 20, 0, NANOSECONDS, new ArrayBlockingQueue<>(5));

        // 在工作线程大于核心线程数参数时，如果提交的数量超过了阻塞队列的最大数量，就会使用最大线程数参数来判断是否可以创建新工作线程来处理任务
        // 工作线程小于核心线程数参数时，是通过核心线程数来判断是否能开启新工作线程的
        //ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 5, 0, NANOSECONDS, new ArrayBlockingQueue<>(20));

        for (int i = 0; i < 20; i++) {
            final String name = i + "";
            System.out.println("pre:" + name);
            threadPool.submit(() -> {
                System.out.println("current:" + name);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("threadPool worker size:" + threadPool.getPoolSize());
        threadPool.shutdown();
    }

    /**
     * 使用栅栏
     */
    public static void testCyclicBarrier() {
        // 闭锁
        CountDownLatch countDownLatch = new CountDownLatch(5);
        // 栅栏 线程相互等待
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            // 指定数量的线程都执行到 await 方法时，会执行这里的操作（如果执行线程数不够设置的数量，则不会触发这里的执行）
            System.out.println("栅栏打开，赛马开始...");
        });
        for (int i = 1; i <= 5; i++) {
            final int t = i;
            new Thread(() -> {
                System.out.println(t + " 号马就绪...");
                try {
                    // 指定数量的线程会在此处相互等待。只有指定数量的线程都执行到此处时，才能继续执行
                    cyclicBarrier.await();

                    Thread.sleep(RandomUtils.nextInt(3) * 1000L);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                System.out.println(t + " 号马到达终点...");
                countDownLatch.countDown();
            }).start();
        }

        try {
            // 等待所有赛马跑完
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("所有赛马都到达终点，比赛结束");
    }

    /**
     * 任务分段计算，结果聚合
     */
    private static void testFutureAndCall() {
        final Integer[] data = new Integer[100000000];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextInt(100);
        }

        long start = System.currentTimeMillis();
        Long sum = 0L;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
        }
        long end = System.currentTimeMillis();
        System.out.println("sum:" + sum + "，执行时间：" + (end -start));

        long start1 = System.currentTimeMillis();
        sum = 0L;
        int culSize = 4000000;
        int taskSize = data.length / culSize + 1;
        List<Future<Long>> resultList = new ArrayList<>();
        ExecutorService exec = Executors.newFixedThreadPool(taskSize);
        for (int i = 0; i < taskSize; i++) {
            final int seg = i;
            Future<Long> res = exec.submit(() -> {
                int start2 = seg * culSize;
                int end1 = Math.min((start2 + culSize), data.length);
                Long segSum = 0L;
                for (int i1 = start2; i1 < end1; i1++) {
                    segSum += data[i1];
                }
                return segSum;
            });
            resultList.add(res);
        }

        exec.shutdown();
        for (Future<Long> longFuture : resultList) {
            try {
                sum += longFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                sum += 0L;
                e.printStackTrace();
            }
        }
        long end1 = System.currentTimeMillis();
        System.out.println("seg sum:" + sum + "，seg执行时间：" + (end1 -start1));
    }

    /**
     * 使用闭锁，闭锁应用的场景：
     * <p>1. 确保某个计算在其需要的所有资源都被初始化之后才继续执行。</p>
     * <p>2. 确保某个服务在其依赖的所有其他服务都已经启动之后才启动。</p>
     * <p>3. 等待直到某个操作的所有参与者都就绪再继续执行。</p>
     */
    public static void testCountDown() {
        // 闭锁 等待事件
        CountDownLatch countDownLatch = new CountDownLatch(2);
        for (int i = 0; i < 4; i++) {
            final int t = i;
            new Thread(() -> {
                System.out.println("countDownLatch thread " + t + "prepared...");
                countDownLatch.countDown();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("countDownLatch thread " + t + "continue...");
            }).start();
        }

        try {
            // 等待 countDownLatch 的数量归0(不归0会一直等待,也可以设置等待的时间)，才会继续往下执行
            countDownLatch.await(2000, TimeUnit.MILLISECONDS);
            System.out.println("线程执行完毕....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 轮询Future结果
     */
    private static void loopGetForFuture() {
        int taskSize = 5;
        ExecutorService executor = Executors.newFixedThreadPool(taskSize);
        List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();

        for (int i = 1; i <= taskSize; i++) {
            int sleep = taskSize - i + 1;
            int value = i;
            //向线程池提交任务
            Future<Integer> future = executor.submit(new ReturnAfterSleepCallable(sleep, value));
            //保留每个任务的Future
            futureList.add(future);
        }
        // 轮询,获取完成任务的返回结果
        while (taskSize > 0) {
            for (Future<Integer> future : futureList) {
                Integer result = null;
                try {
                    result = future.get(100L, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    // no op
                }
                //任务已经完成
                if (result != null) {
                    System.out.println("result = " + result);
                    //从future列表中删除已经完成的任务
                    futureList.remove(future);
                    taskSize--;
                    break;
                }
            }
        }
        // 所有任务已经完成,关闭线程池
        System.out.println("all over ");
        executor.shutdown();
    }

    /**
     * 使用 ExecutorCompletionService 利用阻塞队列获取结果
     */
    private static void getFutureResWithBlockQueue() {
        int taskSize = 5;
        ExecutorService executor = Executors.newFixedThreadPool(taskSize);
        ExecutorCompletionService<Integer> completionExec = new ExecutorCompletionService<>(executor);
        for (int i = 1; i <= taskSize; i++) {
            int sleep = taskSize - i + 1;
            int value = i;

            // 向线程池提交任务
            completionExec.submit(new ReturnAfterSleepCallable(sleep, value));
        }

        try {
            for (int i = 0; i < taskSize; i++) {
                System.out.println("result:" + completionExec.take().get());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 所有任务已经完成,关闭线程池
        System.out.println("all over ");
        executor.shutdown();
    }

    /**
     * 响应中断
     *
     * 对于一些不支持取消但仍可以调用可中断阻塞方法的操作，它们必须在循环中调用这些方法，并在发现中断后重新尝试。
     * 它们应该在本地保存中断状态，并在返回前恢复状态而不是在捕获 InterruptedException 时恢复状态
     */
    private static String getNextVal(BlockingQueue<String> queue) {
        boolean isInterrupt = false;
        try {
            while (true) {
                try {
                    System.out.println(Thread.currentThread().getName() + " 异常捕获前中断标识：" + Thread.currentThread().isInterrupted());
                    return queue.take();
                } catch (InterruptedException e) {
                    // 这里只保存本地的中断状态，还不能恢复中断，因为这里恢复中断会无限循环，而不是阻塞等待结果
                    System.out.println(Thread.currentThread().getName() + " 异常捕获后中断标识：" + Thread.currentThread().isInterrupted());
                    isInterrupt = true;
                }
            }
        } finally {
            // 成功从队列中获取到结果后，再恢复中断状态
            if (isInterrupt) {
                Thread.currentThread().interrupt();
            }
        }
    }



   static class ReturnAfterSleepCallable implements Callable<Integer>{

        private int sleepSeconds;
        private int returnValue;

        public ReturnAfterSleepCallable(int sleepSeconds,int returnValue){
            this.sleepSeconds = sleepSeconds;
            this.returnValue = returnValue;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println("begin to execute ");

            TimeUnit.SECONDS.sleep(sleepSeconds);
            return returnValue;
        }

    }

    public static void main(String[] args) throws Exception {
        // threadPool();
        // testCyclicBarrier();
        // testFutureAndCall();
        // getFutureResWithBlockQueue();

        // 测试响应中断
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(5);
        Thread t1 = new Thread(() -> {
            String data = getNextVal(blockingQueue);
            System.out.println("data：" + data);
            System.out.println(Thread.currentThread().getName() + " 执行返回后的中断标识：" + Thread.currentThread().isInterrupted());
        });

        t1.start();
        // 该方法不支持取消操作，所以线程设置了中断标识后，仍能获取到结果
        t1.interrupt();
        blockingQueue.offer("Tom");
        Thread.sleep(50);
        // 线程执行完run方法后就会消亡，并且会把中断状态置为false，所有这里打印的是false
        System.out.println(t1.getName() + " 中断标识：" + t1.isInterrupted());
    }
}
