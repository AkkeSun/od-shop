package com.account.infrastructure.exception;

import lombok.Getter;

@Getter
public class CustomValidationException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomValidationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
