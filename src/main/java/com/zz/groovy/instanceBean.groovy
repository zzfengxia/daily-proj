package com.zz.groovy

import com.zz.groovy.bean.Order
import com.zz.parsexml.XmlParser

import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Francis.zz on 2016/9/19.
 * 描述：根据Bean与xml的映射关系实例化Bean
 */

def getInstance(xmlName, xmlRequest, bean) {
    def beanName = bean.getName();
    def mappingRoot = new XmlParser().parse(xmlName);

    if(beanName != mappingRoot.@name) {
        println "解析的类名[${beanName}]与映射文件类名[${mappingRoot.@name}]不匹配";
        return null;
    }

    def requestRoot = new XmlParser().parse(xmlRequest);

    def instance = bean.newInstance();
    instanceBean(bean, instance, requestRoot, mappingRoot);

    // 输出实例化Bean的属性和值
    assert instance instanceof Order;

    if(instance instanceof Order) {
        println "-- Order class instance\n";
        outInstanceBean(instance);
    }
    //def path = mappingRoot."Order.nodeId".text();
    /*def str = new StringBuffer(path);
    str.replace(0, str.indexOf("."), "requestRoot");
    println str.toString();
    def realPath = str.toString();
    realPath = realPath.substring(realPath.indexOf(".") + 1)
    println realPath;*/

    // 根据Bean字段映射关系获取请求值
    //path = path.substring(path.indexOf(".") + 1);
    //println requestRoot."${path}".text();

    //def fields = bean.getDeclaredFields();

}

def instanceBean(bean, instance, requestRoot, mappingRoot) {
    println "----------------------"
    println bean.getSimpleName();
    println "请求xml节点:${requestRoot.name()}";
    println "映射xml节点:${mappingRoot.name()}";
    println "----------------------"
    // 获取属性set方法
    Field[] fields = bean.getDeclaredFields();
    for(Field it : fields) {
        def nodeName = "${bean.getSimpleName()}.${it.getName()}";
        println "正在解析节点:${nodeName}"

        PropertyDescriptor pd = new PropertyDescriptor(it.getName(), bean);
        Method method = pd.getWriteMethod();
        if(method == null) {
            println "属性${it.getName()}不存在set方法"
            continue;
        }

        // 获取Bean的属性与requestXml的映射关系所在节点
        def node = mappingRoot."${nodeName}"[0];

        Class fc = it.getType();
        if(fc.isAssignableFrom(List.class)) {
            Type ft = it.getGenericType();
            if(ft instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) ft;
                Class childClass = (Class) pt.getActualTypeArguments()[0];
                List childList = new ArrayList();
                // 获取List属性的映射节点
                def attrNode = node.@name;
                println "List属性映射节点:${attrNode}";
                /*def path = attrNode.substring(attrNode.indexOf(".") + 1);
                println "ListPath:${path}";*/
                def topNodePath = getCompletelyName(requestRoot);
                println "topNode:${topNodePath}";
                def finalPath = attrNode.split(topNodePath)[1].substring(1);
                println "finalPath:${finalPath}";
                def finalRoot = parseNodeByStr(requestRoot, finalPath);
                println "finalRoot:${finalRoot}";
                finalRoot.children().each{ cn->
                    println "cNode:${cn}"
                    // 解析List属性
                    def childInstance = childClass.newInstance();
                    instanceChildBean(childClass, childInstance, cn, node);
                    childList.add(childInstance);
                }
                method.invoke(instance, childList);
            }
        }else if(fc instanceof Integer) {
            // 整形处理
            def path = node.text();
            /*// 获取映射节点的最末节点(与Bean属性名相同)
            def endNode = path.substring(path.lastIndexOf(".") + 1);
            // 除最末节点外的其他节点
            def otherNode = path.substring(0, path.lastIndexOf("."));*/

            path = path.substring(path.indexOf(".") + 1);
            def finalVal = requestRoot."${path}".text();

            method.invoke(instance, finalVal);
        }else{
            def path = node.text();
            /*// 获取映射节点的最末节点(与Bean属性名相同)
            def endNode = path.substring(path.lastIndexOf(".") + 1);*/

            path = path.substring(path.indexOf(".") + 1);
            def finalVal = requestRoot."${path}".text();

            method.invoke(instance, finalVal);
        }
    }
}

def instanceChildBean(bean, instance, requestRoot, mappingRoot) {
    println "----------------------"
    println bean.getSimpleName();
    println "请求xml节点:${requestRoot.name()}";
    println "映射xml节点:${mappingRoot.name()}";
    println "----------------------"
    // 获取属性set方法
    Field[] fields = bean.getDeclaredFields();
    for(Field it : fields) {
        def nodeName = "${bean.getSimpleName()}.${it.getName()}";
        println "正在解析节点:${nodeName}"

        PropertyDescriptor pd = new PropertyDescriptor(it.getName(), bean);
        Method method = pd.getWriteMethod();
        if(method == null) {
            println "属性${it.getName()}不存在set方法"
            continue;
        }

        // 获取Bean的属性与requestXml的映射关系所在节点
        def node = mappingRoot."${nodeName}"[0];

        Class fc = it.getType();
        println "属性类型:${fc.getName()}"
        if(fc.isAssignableFrom(List.class)) {
            Type ft = it.getGenericType();
            if(ft instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) ft;
                Class childClass = (Class) pt.getActualTypeArguments()[0];
                List childList = new ArrayList();
                // 获取List属性的映射节点
                def attrNode = node.@name;
                println "List属性映射节点:${attrNode}";
                /*def path = attrNode.substring(attrNode.indexOf(".") + 1);
                println "ListPath:${path}";*/

                def topNodePath = getCompletelyName(requestRoot);
                println "topNode:${topNodePath}";
                def finalPath = attrNode.split(topNodePath)[1].substring(1);
                println "finalPath:${finalPath}";
                def finalRoot = parseNodeByStr(requestRoot, finalPath);
                finalRoot.children().each{ cn->
                    println "cNode:${cn}"
                    // 解析List属性
                    def childInstance = childClass.newInstance();
                    instanceChildBean(childClass, childInstance, cn, node);
                    childList.add(childInstance);
                }
                method.invoke(instance, childList);
            }
        }else if(fc.getSimpleName() == "Integer") {
            // 整形处理
            def path = node.text();

            def topNodePath = getCompletelyName(requestRoot);
            def finalPath = path.split(topNodePath)[1].substring(1);;
            println "子节点${finalPath}";
            /*// 获取映射节点的最末节点(与Bean属性名相同)
            def endNode = path.substring(path.lastIndexOf(".") + 1);
            // 除最末节点外的其他节点
            def otherNode = path.substring(0, path.lastIndexOf("."));*/

            //path = path.substring(path.indexOf(".") + 1);
            def finalVal = requestRoot."${finalPath}".text();
            println "finalValue:${finalVal}";
            method.invoke(instance, Integer.parseInt(finalVal));
        }else{
            def path = node.text();
            println "path:${path}";
            def topNodePath = getCompletelyName(requestRoot);
            println "topNode:${topNodePath}";
            def finalPath = path.split(topNodePath)[1].substring(1);
            println "子节点${finalPath}";
            /*// 获取映射节点的最末节点(与Bean属性名相同)
            def endNode = path.substring(path.lastIndexOf(".") + 1);
            // 除最末节点外的其他节点
            def otherNode = path.substring(0, path.lastIndexOf("."));*/

            //path = path.substring(path.indexOf(".") + 1);
            def finalVal = requestRoot."${finalPath}".text();
            println "finalValue:${finalVal}";
            method.invoke(instance, finalVal);
        }
    }
}

// 根据当前节点获取其所在xml的完全域
def getCompletelyName(node) {
    if(node.parent() != null) {
        return getCompletelyName(node.parent()) + ".${node.name()}";
    }else {
        return node.name();
    }
}

def parseNodeByStr(root, str) {
    def arrayStr = str.split("\\.");
    arrayStr.each {it ->
        root = root."${it}"[0];
    }
    return root;
}

def outInstanceBean(instance) {
    Field[] fields = instance.class.getDeclaredFields();
    fields.each {it ->
        Class fc = it.getType();

        PropertyDescriptor pd = new PropertyDescriptor(it.getName(), instance.class);
        Method method = pd.getReadMethod();

        if(fc.isAssignableFrom(List.class)) {
            List attrs = method.invoke(instance, null);
            println "List属性:${instance.class.getSimpleName()}.${it.getName()}"
            def index = 1;
            attrs.each { attr ->
                println "--${index++}--";
                outInstanceBean(attr);
            }
        }else {
            println it.getName() + ":" + method.invoke(instance, null);
        }
    }
}