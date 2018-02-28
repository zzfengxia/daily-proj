package com.zz.parsedp.annotation;

import java.lang.annotation.*;

/**
 * Created by Francis.zz on 2017/12/29.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtMethod {
    /**
     * 方法对象
     */
    Class<?> className();

    /**
     * 方法名
     */
    String methodName();

    /**
     * 参数类型数组
     */
    Class[] types() default {};

    /**
     * 参数值域数组(对应配置对象中的属性值)
     */
    String[] args() default {};
}
