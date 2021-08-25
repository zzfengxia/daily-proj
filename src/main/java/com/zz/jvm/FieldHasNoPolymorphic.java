package com.zz.jvm;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-04-20 16:37
 * ************************************
 */
public class FieldHasNoPolymorphic {
    // 静态变量的初始化
    public static int money = 100;
    public static final String name = "hah";

    static class Father {
        public int money = 1;
        {
            System.out.println("block Father int money:" + money);
        }
        public Father() {
            money = 2;
            showMeTheMoney();
        }
        public void showMeTheMoney() {
            System.out.println("I am Father, i have $" + money);
        }
    }
    static class Son extends Father {
        public int money = 3;
        {
            System.out.println("block son int money:" + money);
        }
        public Son() {
            super();
            System.out.println("son int money:" + money);
            money = 4;
            showMeTheMoney();
        }
        public String name = "gb";
        {
            System.out.println("name is :" + name);
        }
        @Override
        public void showMeTheMoney() {
            System.out.println("I am Son, i have $" + money);
        }
    }
    public static void main(String[] args) {
        Father gay = new Son();
        System.out.println("This gay has $" + gay.money);
        // 运行结果
        /* *********************
        * block Father int money:1
        * I am Son, i have $0
        * block son int money:3
        * son int money:3
        * I am Son, i have $4
        * This gay has $2
          ********************* */
    }
}
