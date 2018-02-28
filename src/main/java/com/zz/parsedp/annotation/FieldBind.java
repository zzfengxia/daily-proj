package com.zz.parsedp.annotation;

import java.lang.annotation.*;

/**
 * Created by Francis.zz on 2018/1/2.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldBind {
    /**
     * 实体类名
     */
    String objName();

    /**
     * 属性名
     */
    String fieldName();
}
