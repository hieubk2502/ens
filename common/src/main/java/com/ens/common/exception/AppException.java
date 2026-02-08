package com.ens.common.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;
    private final HttpStatus status;

    public AppException(String messageKey, Object... args) {
        this(messageKey, HttpStatus.BAD_REQUEST, args);
    }

    public AppException(String messageKey, HttpStatus status, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.status = status != null ? status : HttpStatus.BAD_REQUEST;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
