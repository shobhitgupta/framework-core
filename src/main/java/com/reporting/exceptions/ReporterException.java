package com.reporting.exceptions;

public class ReporterException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public ReporterException() {
		super();
	}

	public ReporterException(String paramString) {
		super(paramString);
		this.message = paramString;
	}

	public String toString() {
		return "Custom Reporter Exception " + this.message;
	}
}