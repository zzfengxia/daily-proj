package com.zz.exception;

/**
 * Created by Francis.zz on 2017/7/10.
 */
public class BizException extends RuntimeException {
    private IErrorCode errorCode;
    private Object[] args;

    public BizException(String message) {
        super(message);
        errorCode = ErrorCode.BIZ_ERROR.setMessage(message);
    }
    public BizException(String message, Object... args) {
        super();
        this.errorCode = ErrorCode.BIZ_ERROR.setMessage(message);
        this.args = args;
    }

    public BizException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BizException(IErrorCode errorCode, Object... args) {
        super();
        this.errorCode = errorCode;
        this.args = args;
    }

    public BizException(IErrorCode errorCode, Throwable cause, Object... args) {
        super(cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public String getMessage() {
        if(args != null) {
            return (new StringBuilder()).append("errorCode:[").append(errorCode.getErrorCode()).append("] ").
                    append(String.format(errorCode.getMessage(), args)).toString();
        }else {
            return (new StringBuilder()).append("errorCode:[").append(errorCode.getErrorCode()).append("] ").
                    append(errorCode.getMessage()).toString();
        }
    }

    public String getContent() {
        if(args != null) {
            return String.format(errorCode.getMessage(), args);
        }else {
            return errorCode.getMessage();
        }
    }

    public String getErrorCode() {
        return errorCode.getErrorCode();
    }
}
