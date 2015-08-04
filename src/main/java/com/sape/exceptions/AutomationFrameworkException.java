package com.sape.exceptions;

public class AutomationFrameworkException extends RuntimeException {
    private static final long serialVersionUID = -1334659951758627370L;

    public AutomationFrameworkException() {
        super();
    }

    public AutomationFrameworkException(String message) {
        super(message);
    }

    public AutomationFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutomationFrameworkException(Throwable cause) {
        super(cause);
    }

    protected AutomationFrameworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}