package com.zz.parsedp.annotation;

import java.lang.annotation.*;

/**
 * Created by Francis.zz on 2017/12/28.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseFileAnnotation {
    /**
     * 解析序号，按此排序
     */
    int ord();

    /**
     * 数据类型
     */
    int type() default 0;

    /**
     * 数据长度(字节数)
     */
    int length() default 0;

    /**
     * 流程控制语句类型
     */
    int processType() default 0;

    /**
     * 需要依赖已解析的域，配合processType流程控制
     */
    String dependFiled() default "";

    /**
     * 是否需要校验已读取字节的长度
     */
    boolean checkLength() default false;
}
