package com.zz.dynamicBinding.handler;

import com.zz.dynamicBinding.HandlerConstant;
import com.zz.dynamicBinding.IHandler;

/**
 * Created by Francis.zz on 2016-04-18.
 * 描述：<br/>
 */
public class SumHandler implements IHandler {
    @Override
    public boolean match(String method) {
        return HandlerConstant.SUM.name().equals(method);
    }

    @Override
    public void excute() {
        System.out.println("dynamic method invoking:" + HandlerConstant.SUM.name());
    }
}
