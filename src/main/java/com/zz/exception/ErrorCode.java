package com.zz.exception;

/**
 * Created by Francis.zz on 2017/7/10.
 */
public enum ErrorCode implements IErrorCode {
    BIZ_ERROR("-1", "业务异常"),
    /**
     * 并发冲突异常
     */
    STALE_OBJECT_STATE_EXCEPTION("10", "版本冲突,数据已被其他事务修改"),

    /**
     * 服务器http请求异常
     */
    HTTP_REQUEST_ERROR("20", "http request error, %s"),

    SOCKET_REQUEST_ERROR("21", "socket request error, %s")
    ;

    private String message;
    /**
     * 异常描述，支持String.format格式
     */
    private String errorCode;

    ErrorCode(String errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    public ErrorCode setMessage(String msg) {
        this.message = msg;
        return this;
    }
}
