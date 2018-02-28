package com.zz.groovy

import groovy.xml.MarkupBuilder

import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Francis.zz on 2016/9/14.
 * 描述：Groovy脚本
 */
def printHello(name, date) {
    println "Hello world, ${name}, ${date}";
}

def parseXml(beanClass, xmlPath) {
    //println root.nodeId.hasProperty('directChildren');  // 判断节点是否存在子节点；directChildren为子节点属性
    /*println "firstNode:${root.nodeId.text()}";
    root.orderItems.item.each {
        println it.'@id';
        println "sku:" + it.sku.text();
        println "num:" + it.num.text();
    }

    root.find {it ->
        println it.name();
        if(it.name() == 'orderItems') {
            println it.children().size();
        }
    }*/

    // 解析Bean与xml的映射关系
    def root = new XmlParser().parse(xmlPath);

    def oo = root.orderItems;
    println oo[0];
    println oo.item.skus[0];
    def mapping = [:];  // 定义Map
    checkXmlAndBean(beanClass, root, mapping);

    // 生成映射文件
    def sw = new StringWriter();
    def xmlBuilder = new MarkupBuilder(sw);

    xmlBuilder.beans(name:"${beanClass.getName()}") {
        parseMapToXml(mapping, xmlBuilder);
    }
    def file = new File("D:\\xmlToBeanTest1.xml")
    file.write(sw.toString())
    println "文件生成成功!";
    //findNodeAll(root, "sku");
}

def checkXmlAndBean(beanClass, root, mapping) {
    println "rootNode:${root}";
    Field[] beanFields = beanClass.getDeclaredFields();
    // 获取类名
    def className = beanClass.getSimpleName();
    for(Field field : beanFields) {
        Class fc = field.getType();
        String fName = field.getName();
        println "查找${className}.${fName}";
        /*if(root."${fName}".size() == 0) {

//            println "未找到节点${beanClass.getName()}.${fName}";
//            break;
        }*/
        if(!findNodeAll(root, fName, mapping, className)) {
            println "${className}.${fName}未找到对应节点";
            continue;
        }
        if(fc.isAssignableFrom(List.class)) {
            /*if(root."${fName}".children().size() == 0) {
                println root.name() + ".${fName}没有子节点";
                break;
            }*/
            Type ft = field.getGenericType();
            if(ft instanceof ParameterizedType) {
                ParameterizedType pft = (ParameterizedType) ft;
                Class childClass = (Class) pft.getActualTypeArguments()[0];
                // 获取当前List属性所在节点的根节点
                def currentNode = mapping.get("${className}.${fName}");
                def topNodePath = getCompletelyName(root);
                println "topNode:${topNodePath}";
                def finalPath = currentNode.split(topNodePath)[1].substring(1);
                println "finalPath:${finalPath}";
                println "before:" + root;
                def cRoot = parseNodeByStr(root, finalPath);
                println "after:${cRoot}";
                //println beanClass.getName() + ".${fName}:\${root.${fName}}";
                def childMap = [:];     // 创建子对象的映射关系
                checkXmlAndBean(childClass, cRoot, childMap);
                // 将List属性映射的节点保存下来，key为“属性名.nodeName”
                childMap.put("${className}.${fName}.nodeName", mapping.get("${className}.${fName}"));
                mapping.put("${className}.${fName}", childMap);
            }
            continue;
        }

        /*if(root."${fName}".text() != null) {
            // TODO 子节点的text也会被查出来，需要另外的方法
            println beanClass.getName() + ".${fName}:\${root.${fName}.text()}";
        }else if(root."${fName}".@name != null) {
            println beanClass.getName() + ".${fName}:\${root.${fName}.@name}";
        }else {
            println "${fName}没有赋值";
        }*/
    }
}
/**
 *
 * @param root xml节点
 * @param nodeName 字段名
 * @param mapping 返回字段与xml节点的映射结果
 * @param className 类名
 * @return 是否存在类属性名对应的节点
 */
def findNodeAll(root, nodeName, mapping, className) {
    println "currentNode:${root}";
    println "currentMap:${mapping}";
    if (root."${nodeName}".size() > 0) {
        // key为“类名.属性名”的格式
        mapping.put("${className}.${nodeName}", getCompletelyName(root) + ".${nodeName}");
        //println getCompletelyName(root) + ".${nodeName}";
    } else {
        findNode(root, nodeName, mapping, className);
    }
    if(mapping.get("${className}.${nodeName}") == null) {
        println "节点${nodeName}不存在";
        return false;
    }
    return true;
}

def findNode(node, nodeName, mapping, className) {
    if(node."${nodeName}".size() == 0) {
        node.each{ it ->        // ->之前的变量表示闭包声明的参数，it可以不声明
            if(it.hasProperty('directChildren')) {  // 判断当前xml节点是否存在子节点
                if (it."${nodeName}".size() > 0) {
                    //println it.name() + ".${nodeName}";
                    mapping.put("${className}.${nodeName}", getCompletelyName(it) + ".${nodeName}");
                    //println getCompletelyName(it) + ".${nodeName}";
                } else {
                    findNode(it, nodeName, mapping, className);
                }
            }

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
/**
 * 递归遍历map生成xml信息(将xmlBuilder构造器传入即可)
 * @param mapping Map对象
 * @param xml  xml构造器
 * @return
 */
def parseMapToXml(mapping, xml) {
    mapping.each {entry ->
        if(entry.value.getClass().getSimpleName() == 'LinkedHashMap') {
            def keyName = entry.key + ".nodeName";
            def childMap = entry.value;
            def attrVal = childMap.get(keyName);
            // 删除map中key为keyName的键值对
            entry.value.remove("${keyName}");
            xml."${entry.key}"(name:"${attrVal}"){
                parseMapToXml(entry.value, xml)
            }
        }else {
            xml."${entry.key}"("${entry.value}");
        }
    }
}

def parseNodeByStr(root, str) {
    def arrayStr = str.split("\\.");
    arrayStr.each {it ->
        root = root."${it}"[0];
    }
    return root;
}