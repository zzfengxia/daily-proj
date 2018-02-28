package com.zz.vfc.daily;

import com.zz.vfc.CatObj;
import org.junit.Test;

/**
 * Created by Francis.zz on 2017/7/20.
 */
public class StaticObjTest {
    @Test
    public void testStatic() {
        CatObj cat1 = new CatObj("huang", "white");
        CatObj cat2 = new CatObj("hua", "white");
        CatObj cat3 = new CatObj("mei", "black");

        System.out.println(CatObj.num);
    }
}
