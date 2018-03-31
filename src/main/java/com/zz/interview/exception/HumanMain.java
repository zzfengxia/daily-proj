package com.zz.interview.exception;

import org.junit.Test;

/**
 * --------------------------------
 * create by Intellij IDEA.
 *
 * @author Francis.zz
 * @date 2018-03-28 10:58
 * --------------------------------
 */
public class HumanMain {
    @Test
    public void testExcepionOrder() {
        try {
            try {
                throw new Sneeze();
            } catch (Annoyance a) {
                System.out.println("Caught Annoyance");
                throw a;
            }
        } catch (Sneeze s) {
            System.out.println("Caught Sneeze");
            return;
        } finally {
            System.out.println("Hello World!");
        }
    }

    /**
     * try...finally return执行先后问题
     *
     * try中有return语句时，finally语句仍然会被执行。return的值先保存起来，再执行finally(return)，最后返回
     */
    @Test
    public void testException() {
        System.out.println(exceHandle());
    }

    private String exceHandle() {
        String result;
        try {
            result = "1";
            return result;
        }finally {
            result = "2";
            System.out.println("finally executing...");
        }
    }
}
