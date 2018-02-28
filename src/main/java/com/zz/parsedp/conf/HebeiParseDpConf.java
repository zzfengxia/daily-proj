package com.zz.parsedp.conf;


import com.zz.parsedp.annotation.ExtMethod;

import java.lang.reflect.Method;

/**
 * Created by Francis.zz on 2017/12/29.
 */
public class HebeiParseDpConf {
    private String mic;
    private String lcca;
    private String aidNum;


    public static void main(String[] args) throws Exception {
        ExtMethod extMethod = HebeiParseDpConf.class.getDeclaredField("dgiValue").getAnnotation(ExtMethod.class);
        if(extMethod != null) {
            Class class1 = extMethod.className();
            String method = extMethod.methodName();
            Method m = class1.getDeclaredMethod(method);
            m.invoke(null, null);

            Class[] types = extMethod.types();
            m = class1.getDeclaredMethod(method, types);
            m.invoke(null, "String");
        }

    }
}
