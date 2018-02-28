package com.zz.parsexml;

/**
 * 异常
 *  Date          Author       Version       Description
 * ----------------------------------------------------------
 *  2015-1-9      CC            1.0           Create
 */
public class PaiUException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PaiUException(IErrorCode code) {
		this.code = code;
	}

	public PaiUException(IErrorCode code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public PaiUException(IErrorCode code, String... formatArgs) {
		super();
		this.code = code;
		this.formatArgs = formatArgs;
	}

	public PaiUException(IErrorCode code, Throwable cause, String... formatArgs) {
		super(cause);
		this.code = code;
		this.formatArgs = formatArgs;
	}

	public String getMessage() {
		String errorCode = getErrorCode();
		if (formatArgs != null)
			return (new StringBuilder()).append("[").append(errorCode).append("] ").append(String.format(code.getMessage(), (Object[]) formatArgs)).toString();
		else
			return (new StringBuilder()).append("[").append(errorCode).append("] ").append(code.getMessage()).toString();
	}
	
	public String getContent(){
		if (formatArgs != null)
			return (new StringBuilder()).append(String.format(code.getMessage(), (Object[]) formatArgs)).toString();
		else
			return (new StringBuilder()).append(code.getMessage()).toString();
	}
	
	public String getErrorCode() {
		return code.getCode();
	}

	private final IErrorCode code;
	private String formatArgs[];

}
