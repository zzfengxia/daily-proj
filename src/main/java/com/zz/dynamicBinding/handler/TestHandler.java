package com.zz.dynamicBinding.handler;

import com.zz.dynamicBinding.HandlerConstant;
import com.zz.dynamicBinding.IHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Francis.zz on 2016-04-18.
 * 描述：测试Handler通过match方法匹配不同的Handler类 <br/>
 */
public class TestHandler {
    private Class[] handlers = {AddHandler.class, SayHelloHandler.class, SumHandler.class};

    @Before
    public void setUp() {
    }

    @Test
    public void testHandler() throws IllegalAccessException, InstantiationException {
        if(handlers != null) {
            for(Class cls : handlers) {
                IHandler handler = (IHandler) cls.newInstance();
                for(HandlerConstant method : HandlerConstant.values()) {
                    if(handler.match(method.name())) {
                        handler.excute();
                    }
                }
            }
        }
    }
}
