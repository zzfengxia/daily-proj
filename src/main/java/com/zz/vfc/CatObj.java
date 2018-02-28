package com.zz.vfc;

import lombok.Data;

/**
 * Created by Francis.zz on 2017/7/20.
 */
@Data
public class CatObj {
    public static int num = 0;

    private String name;
    private String color;

    public CatObj(String name, String color) {
        this.name = name;
        this.color = color;
        num += 1;
    }
}
