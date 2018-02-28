package com.zz.parsedp;

/**
 * Created by Francis.zz on 2018/1/2.
 * 通用解析配置相关常量
 */
public class ConfigConstant {
    /* ***** 数据类型type start ********/
    public static final int DATA_TYPE_ASCII         = 1;
    public static final int DATA_TYPE_BINARY_INT    = 2;
    public static final int DATA_TYPE_BINARY_LONG   = 3;
    public static final int DATA_TYPE_BCD           = 4;

    /** BCD，BINARY类型里有些需要转成HEX字符串 */
    public static final int DATA_TYPE_HEX           = 5;

    /** 循环体 **/
    public static final int DATA_TYPE_BODY          = 0;
    /* ***** 数据类型type end ********/

    /* ***** 流程控制类型processType start ********/
    /** 顺序语句 */
    public static final int PROCESS_TYPE_INORDER    = 0;
    /** while语句 */
    public static final int PROCESS_TYPE_WHILE1     = 1;
    public static final int PROCESS_TYPE_WHILE2     = 2;
    public static final int PROCESS_TYPE_WHILE3     = 3;

    /** for循环 */
    public static final int PROCESS_TYPE_FOR        = 4;
    /* ***** 流程控制类型processType end ********/

}
