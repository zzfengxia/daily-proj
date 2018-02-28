package com.zz.dynamicBinding;

/**
 * Created by Francis.zz on 2016-04-18.
 * 描述：处理类接口，所有处理类的超类 <br/>
 */
public interface IHandler {
    /**
     * 通过相关参数动态匹配需要的处理类
     * @return
     */
    public boolean match(String method);

    /**
     * 执行相关操作的方法
     */
    public void excute();
}
