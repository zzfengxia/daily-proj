package com.zz.dailytest;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Francis.zz on 2016-04-14.
 * 描述：使用反射机制获取泛型入参的类型 <br/>
 */
public class TestGetGeneric<T> {
    Type type;

    protected TestGetGeneric() {
        this.type = getParametricType(this.getClass());
    }
    private Type getParametricType(Class<?> subClass) {
        Type superClass = subClass.getGenericSuperclass();
        ParameterizedType pType = (ParameterizedType) superClass;
        return pType.getActualTypeArguments()[0];
    }
    public Type getType() {
        return type;
    }

    public static void main(String[] args) {
        Type type = new TestGetGeneric<Map<String, String>>(){}.getType();
        if(type instanceof ParameterizedTypeImpl) {
            ParameterizedTypeImpl typeStr = (ParameterizedTypeImpl) type;
            Type[] types = typeStr.getActualTypeArguments();
            for(Type p : types) {
                if(p instanceof Class) {
                    Class c = (Class) p;
                    System.out.println("泛型实际类型：" + c.getName());
                }
            }
            String rowType = typeStr.getRawType().getName();
            System.out.println("rowType：" + rowType);
        }
    }
}
