package com.zz.groovy.bean;

import java.util.List;

/**
 * Created by Francis.zz on 2016/9/14.
 * 描述：
 */
public class Order {
    String nodeId;
    List<OrderItem> orderItems;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
