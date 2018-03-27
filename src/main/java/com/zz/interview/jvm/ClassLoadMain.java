package com.zz.interview.jvm;

import org.junit.Test;

/**
 * 类加载测试
 * --------------------------------
 * create by Intellij IDEA.
 * @author Francis.zz
 * @date 2018-03-27 15:33
 * --------------------------------
 */
public class ClassLoadMain {
    /**
     * 类加载顺序
     *
     * 静态变量、静态初始化块–>变量、初始化块–> 构造器
     * 父类static方法/块 –> 子类static方法/块 –> 父类初始化块 -> 父类构造方法-> 子类初始化块 -> 子类构造方法 -> 成员方法
     */
    @Test
    public void testClassLoadOrder() {
        SubClass subClass = new SubClass();
        subClass.init();
    }
}
