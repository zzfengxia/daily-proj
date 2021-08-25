package com.zz.juc.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-07-05 09:31
 * ************************************
 */
public class Account {
    private final Lock lock = new ReentrantLock();

    // Condition对象
    private final Condition condDeposit = lock.newCondition();
    private final Condition condWithdraw = lock.newCondition();
    private int balance;

    public Account(int balance) {
        this.balance = balance;
    }

    //取钱
    public void withdraw(int drawAmount) {
        lock.lock();
        try {
            //如果账户余额不足，则取现方法阻塞
            while (balance < drawAmount) {
                System.out.println(Thread.currentThread().getName() + "取钱阻塞");
                // 会释放持有的锁
                condWithdraw.await();
            }
            //执行取钱
            balance -= drawAmount;
            System.out.println(Thread.currentThread().getName() + " 取钱:" + drawAmount + " 账户余额为：" + balance);
            //唤醒存钱线程
            condDeposit.signal();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " 解锁");
        }
    }

    //存钱
    public void deposit(int depositAmount) {
        lock.lock();
        try {
            //如果账户余额大于1000，存钱方法阻塞
            while (balance > 1000) {
                System.out.println(Thread.currentThread().getName() + "存钱阻塞");
                condDeposit.await();
            }
            balance += depositAmount;
            System.out.println(Thread.currentThread().getName() + " 存款:" + depositAmount + " 账户余额为：" + balance);
            //唤醒取钱线程
            condWithdraw.signal();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " 解锁");
        }
    }
}
