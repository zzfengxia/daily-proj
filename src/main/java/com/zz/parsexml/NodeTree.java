package com.zz.parsexml;

import java.util.Map;

/**
 * Created by Francis.zz on 2016/9/14.
 * 描述：节点树
 */
public class NodeTree {
    // 节点名
    private String nodeName;

    // 节点内容
    private String nodeText;

    // 节点属性
    private Map<String, String> nodeAttr;

    // 子节点
    private Map<String, NodeTree> childNode;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeText() {
        return nodeText;
    }

    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
    }

    public Map<String, String> getNodeAttrs() {
        return nodeAttr;
    }

    public void setNodeAttr(Map<String, String> nodeAttr) {
        this.nodeAttr = nodeAttr;
    }

    public void addAttr(String name, String value) {
        if(this.nodeAttr.get(name) != null) {
            System.out.println("已存在相同的属性" + name);
        }
        this.nodeAttr.put(name, value);
    }

    public String getAttr(String name) {
        return this.nodeAttr.get(name);
    }

    public Map<String, NodeTree> getChildNodes() {
        return childNode;
    }

    public void addChildNode(NodeTree node) {
        String childName = node.getNodeName();
        if(childNode.get(childName) != null) {
            System.out.println("已存在相同名称的子节点" + childName);
        }
        childNode.put(node.getNodeName(), node);
    }

    public NodeTree getChildNode(String name) {
        return this.childNode.get(name);
    }
}
