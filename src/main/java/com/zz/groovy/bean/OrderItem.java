package com.zz.groovy.bean;

import java.util.List;

/**
 * Created by Francis.zz on 2016/9/14.
 * 描述：
 */
public class OrderItem {
    private String sku;
    private Integer num;
    private List<Sku> skus;
    private List<SkuItem> skuItems;

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<SkuItem> getSkuItems() {
        return skuItems;
    }

    public void setSkuItems(List<SkuItem> skuItems) {
        this.skuItems = skuItems;
    }
}
