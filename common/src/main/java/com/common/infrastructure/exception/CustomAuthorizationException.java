package com.common.infrastructure.exception;

import lombok.Getter;

@Getter
public class CustomAuthorizationException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomAuthorizationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
