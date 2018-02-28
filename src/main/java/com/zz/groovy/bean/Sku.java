package com.zz.groovy.bean;

/**
 * Created by Francis.zz on 2016/9/19.
 * 描述：
 */
public class Sku {
    private String sku;
    private String skuName;
    private Integer num;
    //private List<SkuItem> skuItems;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    /*public List<SkuItem> getSkuItems() {
        return skuItems;
    }

    public void setSkuItems(List<SkuItem> skuItems) {
        this.skuItems = skuItems;
    }*/
}
