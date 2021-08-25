package com.zz.juc.condition;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-07-05 09:31
 * ************************************
 */
public class DepositDrawTest {
    public static void main(String[] args) {
        // 创建一个账户，初始账户余额为0
        Account acct = new Account(0);
        new DrawThread("取钱者1", acct, 400).start();
        new DrawThread("取钱者2", acct, 800).start();
        new DepositThread("存款者甲", acct, 600).start();
        new DepositThread("存款者乙", acct, 800).start();
        new DepositThread("存款者丙", acct, 400).start();
    }

    static class DrawThread extends Thread {
        // 模拟用户账户
        private Account account;
        // 每次取数数
        private int drawAmount;

        public DrawThread(String name, Account account, int drawAmount) {
            super(name);
            this.account = account;
            this.drawAmount = drawAmount;
        }

        public void run() {
            for (int i = 0; i < 3; i++) {
                account.withdraw(drawAmount);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class DepositThread extends Thread {
        // 模拟用户账户
        private Account account;
        // 每次存钱数
        private int depositAmount;

        public DepositThread(String name, Account account, int depositAmount) {
            super(name);
            this.account = account;
            this.depositAmount = depositAmount;
        }

        public void run() {
            for (int i = 0; i < 3; i++) {
                account.deposit(depositAmount);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
