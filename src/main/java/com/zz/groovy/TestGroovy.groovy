package com.zz.groovy

import com.zz.groovy.bean.Order
import groovy.xml.MarkupBuilder
import org.junit.Test

import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Francis.zz on 2016/9/14.
 * 描述：Groovy Class; Groovy 的HelloWorld
 */
class TestGroovy {
    public static void main(String[] args) {
        def aa = "this is a groovy class";
        // 要使用双引号表达式${} 才能生效
        println("Hello world, ${aa}!");
    }

    // groovy Map相关
    /**
     * 1 groovy的map可以使用map.key,map.value的形式访问
     * 2 groovy的map key同样是唯一的
     */
    @Test
    public void testGroovyMap() {
        def gm = [:];
        def cgm = [cname : 'jerry'];
        gm.put("name", "Tom");
        gm.put("name", "Bomy");
        gm.name = cgm;

        //println gm.getClass   // 打印java.util.LinkedHashMap
        def xmlBuilder = new MarkupBuilder();
        xmlBuilder.beans() {
            parseMapToXml(gm, xmlBuilder);
        }

        //println gm.name.class;
    }
    // 递归遍历map生成xml信息(将xmlBuilder构造器传入即可)
    def parseMapToXml(mapping, xml) {
        mapping.each {entry ->
            if(entry.value.getClass().getSimpleName() == 'LinkedHashMap') {
                xml."${entry.key}"(){
                    parseMapToXml(entry.value, xml)
                }
            }else {
                xml."${entry.key}"("${entry.value}");
            }
        }
    }

    @Test
    public void testInstanceBean() {
        def bean = Order.class;
        Object instance = bean.newInstance();
        // Bean属性赋值
        instanceBean(Order.class, instance);
        if(instance instanceof Order) {
            println instance.getNodeId();
            def index = 1;
            instance.getOrderItems().each {
                println "--------------${index++}---------------"
                println it.getSku();
                println it.getNum();
                it.getSkus().each {s ->
                    println "--------------sku${index++}---------------"
                    println s.getSku();
                    println s.getSkuName();
                }
            };
        }
    }

    def instanceBean(bean, instance) {
        //Class bean = Order.class;
        println bean.getSimpleName();
        // 获取属性set方法
        Field[] fields = bean.getDeclaredFields();
        for(Field it : fields) {
            println "属性类型:${it.getType()}"
            PropertyDescriptor pd = new PropertyDescriptor(it.getName(), bean);
            Method method = pd.getWriteMethod();
            if(method == null) {
                println "属性${it.getName()}不存在set方法"
                continue;
            }
            Class fc = it.getType();
            if(fc.isAssignableFrom(List.class)) {
                Type ft = it.getGenericType();
                if(ft instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) ft;
                    Class childClass = (Class) pt.getActualTypeArguments()[0];
                    def childInstance = childClass.newInstance();
                    instanceBean(childClass, childInstance);
                    List childList = new ArrayList();
                    childList.add(childInstance);
                    method.invoke(instance, childList);
                }
            }else if(fc.isPrimitive()) {
                // 基础类型
                method.invoke(instance, 10);
            }else{
                method.invoke(instance, "123");
            }
        }
    }

    def makeSetMethod(bean, fieldName) {
        // 属性名首字母大写
        fieldName = new StringBuffer().append(Character.toUpperCase(fieldName.charAt(0))).append(fieldName.substring(1)).toString();
        fieldName = "set" + fieldName;
        println fieldName;
        Method method = bean.getDeclaredMethod(fieldName, java.lang.Object.class);
        if(method == null) {
            println "未找到${fieldName}方法";
            return null;
        }
        return method;
    }

    @Test
    public void testMethod() {
        String aa = "aa.bb.cc.dd";
        println aa.substring(aa.lastIndexOf(".") + 1);
        println aa.substring(0, aa.lastIndexOf("."));

        def tm = [aa:"tom",bb:"jerry"];
        println tm["aa"];
        println tm.size();
        tm.remove("aa");
        println tm.size();
        println "${aa}";

        def root = new XmlParser().parse("D:\\xmlToBeanTest1.xml")
        println root."Order.orderItems"[0].attribute("name");
        println root."Order.orderItems"[0].@name;

        def path = "xml.orderItems.item.sku";
        def topNodePath = "xml.orderItems";
        println path.indexOf("${topNodePath}");
        def finalPath = path.substring(path.indexOf("${topNodePath}") + 1);
        println finalPath;
    }

    @Test
    public void testSplit() {
        def str = "aa";
        println str.split("\\.").size();
    }
}

