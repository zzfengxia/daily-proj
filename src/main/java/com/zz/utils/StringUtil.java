package com.zz.utils;

import com.zz.bean.Person;
import com.zz.bean.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Francis.zz on 2016-04-26.
 * 描述：字符串相关通用操作类 <br/>
 */
public class StringUtil {

    /**
     * 字符串数组转换为字符串 <br/>
     * @param data 转换数组
     * @param separator 分隔符
     * @return
     */
    public static String arrayToString(String[] data, String separator) {
        separator = separator == null ? "" : separator;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < data.length; i++) {
            if(i == data.length -1) {
                sb.append(data[i]);
                break;
            }
            sb.append(data[i] + separator);
        }

        return sb.toString();
    }

    /**
     * 使用指定的分隔符将列表合并成一个字符串
     *
     * @param content   内容，Iterable的实现类
     * @param separator 分隔符
     * @return
     */
    public static String join(Iterable content, String separator) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if(separator == null) {
            separator = "";
        }

        // todo 实现各种对象转为字符串的功能
        Object value;
        for (Iterator it = content.iterator(); it.hasNext(); sb.append(value.toString())) {
            value = it.next();
            if(first) {
                first = false;
            } else {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    /**
     * 使用指定的分隔符将数组合并成一个字符串
     *
     * @param content   内容，数组
     * @param separator 分隔符
     * @return
     */
    public static String join(Object[] content, String separator) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if(separator == null) {
            separator = "";
        }

        for (Object o : content) {
            if(first) {
                first = false;
            }else {
                sb.append(separator);
            }

            sb.append(o.toString());
        }

        return sb.toString();
    }


    /**
     * 字符串是否为空，有空格存在则为false
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Map是否为空，有待扩展
     * @param obj
     * @return
     */
    public static boolean isEmpty(Map obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * 将一个对象的属性值赋值另一个对象(属性名相同)
     * TODO 如果有继承的属性则需另外处理;是否排除原对象属性值为空的情况
     * @param src
     * @param target
     * @throws Exception
     */
    public static void copyBean(Object src, Object target) throws Exception {
        Class srcClass = Class.forName(src.getClass().getName());
        Class tarClass = Class.forName(target.getClass().getName());

        Field[] srcField = getAllFileds(srcClass);
        Field[] tarField = getAllFileds(tarClass);

        for(Field f : tarField) {
            String fName = f.getName();
            for(Field s : srcField) {
                if(fName.equals(s.getName())) {
                    // 调用原对象的get方法
                    Object value = invokeGet(src, s.getName(), null);
                    // 调用目标对象的set方法
                    Object[] args = new Object[1];
                    args[0] = value;
                    invokeSet(target, fName, args);
                }
            }
        }
    }

    /**
     * 调用对象的getXXX方法
     * @param obj
     * @param name
     * @param args
     * @return
     * @throws Exception
     */
    private static Object invokeGet(Object obj, String name, Object[] args) throws Exception {
        Class cla = Class.forName(obj.getClass().getName());
        Class superCla = cla.getSuperclass();
        // 获取继承来的方法
        Method[] superMethod = null;
        if(superCla != null) {
            superMethod = superCla.getDeclaredMethods();
        }
        String finalName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        Method[] method = cla.getDeclaredMethods();

        Method[] allMethod = copyArray(method, superMethod);
        Object result = null;
        for(Method e : allMethod) {
            if(finalName.equals(e.getName())) {
                result = e.invoke(obj, args);
                System.out.println("invoke object[" + cla.getName() + "]method：" + e.getName());
                break;
            }
        }

        return result;
    }

    /**
     * 调用对象的setXXX方法
     * @param obj
     * @param name
     * @param args
     * @throws Exception
     */
    private static void invokeSet(Object obj, String name, Object[] args) throws Exception {
        Class cla = Class.forName(obj.getClass().getName());
        String finalName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        // 获取属性类型
        Class fieldType = null;
        Field[] allField = getAllFileds(cla);
        for(Field f : allField) {
            if(name.equals(f.getName())) {
                fieldType = f.getType();
                break;
            }
        }
        // 获取继承来的方法
        Class superCla = cla.getSuperclass();
        Method[] superMethod = null;
        if(superCla != null) {
            superMethod = superCla.getDeclaredMethods();
        }
        Method[] method = cla.getDeclaredMethods();
        Method[] allMethod = copyArray(method, superMethod);

        for(Method e : allMethod) {
            Class[] pTypes = e.getParameterTypes();
            if(pTypes != null && pTypes.length > 0) {
                Class pType = pTypes[0];
                // 比较方法名和参数类型
                if(finalName.equals(e.getName()) && pType.getName().equals(fieldType.getName())) {
                    e.getParameterTypes();
                    e.invoke(obj, args);
                    System.out.println("invoke object[" + cla.getName() + "]method：" + e.getName());
                    break;
                }
            }
        }
    }

    /**
     * 获取对象所有的属性，包括继承来的
     * @param cla
     * @return
     * @throws Exception
     */
    private static Field[] getAllFileds(Class cla) throws Exception {
        Class superCla = cla.getSuperclass();
        Field[] superFiled = null;
        if(superCla != null) {
            superFiled = superCla.getDeclaredFields();
        }

        Field[] fields = cla.getDeclaredFields();
        // get all fields
        Field[] result = copyArray(fields, superFiled);

        return result;
    }

    /**
     * copy arrays
     * @param first
     * @param others
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T[] copyArray(T[] first, T[]... others) throws Exception {
        // TODO 判断类型是否相同 first.getClass().getComponentType();
        int fLen = first.length;
        int total = 0;
        if(others != null) {
            for(T[] o : others) {
                if(o == null) {
                    continue;
                }
                total += o.length;
            }
        }
        T[] result = Arrays.copyOf(first, fLen + total);
        // copy first array
        System.arraycopy(first, 0, result, 0, fLen);
        int offset = fLen;
        // copy others
        for(T[] o : others) {
            System.arraycopy(o, 0, result, offset, o.length);
            offset += o.length;
        }

        return result;
    }

    @Test
    public void testCopyBean() {
        User srcUser = new User();
        srcUser.setUsername("zzxia");
        srcUser.setNickName("風俠");
        srcUser.setEmail("1234@163.com");
        srcUser.setContactPhone("18637262930");
        srcUser.setBirthDay(new Date());
        srcUser.setWeight("180");
        Person person = new Person();
        try {
            copyBean(srcUser, person);

            // 使用反射输出对象所有属性的值
            System.out.println("-------------Person 赋值后-------------");
            for (Field f : getAllFileds(person.getClass())) {
                String fName = f.getName();
                f.setAccessible(true);
                System.out.println(fName + "：" + f.get(person));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testArrayCopy() {
        Integer[] a = {1,3,2,4};
        Integer[] b = {1,3,};
        Integer[] c = {2,5};

        try {
            Integer[] res = copyArray(a, b, c);
            for(int i : res) {
                System.out.print(i + "\t");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 填充字符串(补全)
     *
     * @param origin 原始数据
     * @param length 预期长度
     * @param fillingChar 填充字符
     * @param model  模式;1:左补 0:右补
     * @return
     */
    public static String completeString(String origin, int length, char fillingChar, int model) {
        int diffLen = length - origin.length();
        if(diffLen <= 0) {
            return origin;
        }
        StringBuilder sb = new StringBuilder();
        if(model == 0) {
            sb.append(origin);
        }
        for (int i = 0; i < diffLen; i++) {
            sb.append(fillingChar);
        }
        if(model == 1) {
            sb.append(origin);
        }

        return sb.toString();
    }
}
