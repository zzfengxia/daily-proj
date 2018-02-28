package com.zz.parsexml;

/**
 * Created by Francis.zz on 2016/9/14.
 * 描述：
 */
public enum ParseException implements IErrorCode {
    ERR_NOT_FIND_NODE("5001", "未找到节点信息");

    ParseException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
