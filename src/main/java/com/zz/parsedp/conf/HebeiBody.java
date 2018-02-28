package com.zz.parsedp.conf;

import com.zz.parsedp.ConfigConstant;
import com.zz.parsedp.annotation.BaseFileAnnotation;

/**
 * Created by Francis.zz on 2018/1/2.
 */
public class HebeiBody {
    @BaseFileAnnotation(ord = 1, type = ConfigConstant.DATA_TYPE_BINARY_INT, length = 1)
    private String aidNum;

    @BaseFileAnnotation(ord = 2, type = ConfigConstant.DATA_TYPE_ASCII, length = 16, processType = ConfigConstant.PROCESS_TYPE_FOR,
                        dependFiled = "aidNum")
    private String aid;


}
