package com.zz.dailytest;

/**
 * ************************************
 * create by Intellij IDEA
 *
 * @author Francis.zz
 * @date 2021-07-16 17:19
 * ************************************
 */
public class ObjQuoteTest {
    static class Cat {
        private String name;
        private String color;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    private static void initCat(Cat cat) {
        cat.setName("Tom");
        cat.setColor("black");
    }

    public static void main(String[] args) {
        Cat cat = new Cat();
        Cat catCopy = cat;
        initCat(cat);

        System.out.println(catCopy.getName());
    }
}
