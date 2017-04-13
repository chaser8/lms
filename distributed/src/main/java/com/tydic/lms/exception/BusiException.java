package com.tydic.lms.exception;

public class BusiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String exceptionCd = "0000";
	private String exceptionMsg = "";
	
	public BusiException(String cd, String msg) {
		this.exceptionCd = cd;
		this.exceptionMsg = msg;
	}
	public BusiException(String msg) {
		this.exceptionMsg = msg;
	}

	public String getExceptionCd() {
		return exceptionCd;
	}

	public void setExceptionCd(String exceptionCd) {
		this.exceptionCd = exceptionCd;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
}
