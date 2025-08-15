package com.order.infrastructure.exception;

import lombok.Getter;

@Getter
public class CustomAuthorizationException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomAuthorizationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
