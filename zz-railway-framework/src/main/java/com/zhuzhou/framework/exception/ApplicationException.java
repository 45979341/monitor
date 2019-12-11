package com.zhuzhou.framework.exception;


/**
 * 自定义系统异常
 */
public class ApplicationException extends RuntimeException{

	private static final long serialVersionUID = -7883840067913852752L;

	private String code;

	public ApplicationException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ApplicationException(ErrorResponseMsgEnum msgEnum) {
		super(msgEnum.getMessage());
		this.code = msgEnum.getCode();
	}

	public String getCode() {
		return code;
	}
}
