package com.zz.parsedp.conf;


import com.zz.parsedp.ConfigConstant;
import com.zz.parsedp.annotation.BaseFileAnnotation;

/**
 * Created by Francis.zz on 2018/1/2.
 */
public class HebeiHead {
    @BaseFileAnnotation(ord = 1, type = ConfigConstant.DATA_TYPE_ASCII, length = 7)
    private String mic;
    @BaseFileAnnotation(ord = 2, type = ConfigConstant.DATA_TYPE_BINARY_LONG, length = 8)
    private String lcca;

    @BaseFileAnnotation(ord = 3, processType = ConfigConstant.PROCESS_TYPE_WHILE1, dependFiled = "lcca")
    private HebeiBody body;
}
