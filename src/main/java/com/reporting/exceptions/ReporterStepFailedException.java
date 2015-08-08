package com.reporting.exceptions;

public class ReporterStepFailedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReporterStepFailedException() {
		super();
	}

	public ReporterStepFailedException(String paramString) {
		super(paramString);
	}

	public String toString() {
		return "[Step Failed Exception]";
	}
}

