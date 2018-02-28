package com.zz.groovy;

import com.zz.groovy.bean.Order;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.List;

/**
 * Created by Francis.zz on 2016/9/14.
 * 描述：java类调用groovy脚本Demo
 */
public class InvokeGroovy {
    @Test
    public void testInvoke() {
        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        try {
            Class groovyClass = classLoader.parseClass(new GroovyCodeSource(new File("com/zz/groovy/instanceBean.groovy")));
            GroovyObject instance = (GroovyObject) groovyClass.newInstance();
            // 调用方法传递多个参数, 跟据参数名和参数类型获取方法
            /*Method method = groovyClass.getDeclaredMethod("printHello", Object.class, Object.class);
            method.invoke(instance, "Tom", new Date());*/

            Method method = groovyClass.getDeclaredMethod("parseXml", Object.class, Object.class);
            method.invoke(instance, Order.class, "D:\\requestXmlDemo2.xml");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInstanceBean() {
        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        try {
            Class groovyClass = classLoader.parseClass(new GroovyCodeSource(new File("com/zz/groovy/instanceBean.groovy")));
            GroovyObject instance = (GroovyObject) groovyClass.newInstance();
            Method method = groovyClass.getDeclaredMethod("getInstance", Object.class, Object.class, Object.class);
            InputStream xmlInput = new FileInputStream(new File("D:\\requestXmlDemo2.xml"));
            method.invoke(instance, "D:\\xmlToBeanTest1.xml", xmlInput, Order.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetField() {
        parseClass(Order.class);
    }

    private void parseClass(Class bean) {
        Field[] fields = bean.getDeclaredFields();
        for(Field f : fields) {
            Class fc =  f.getType();
            if(fc.isAssignableFrom(List.class)) {
                Type gc = f.getGenericType();
                if(gc instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) gc;
                    Class parameterClass = (Class) pt.getActualTypeArguments()[0];
                    System.out.println(f.getName() + ":" + parameterClass.getName());
                    parseClass(parameterClass);
                    continue;
                }
            }
            System.out.println(f.getName() + ":" + f.getType().getName());
        }
    }
}
