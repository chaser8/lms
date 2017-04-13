package com.tydic.lms.exception;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String exceptionMsg = "";
	
	public ApplicationException(String msg) {
		super(msg);
		this.exceptionMsg = msg;
	}


	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
}
