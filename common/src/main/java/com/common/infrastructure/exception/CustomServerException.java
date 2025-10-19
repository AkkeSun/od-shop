package com.common.infrastructure.exception;

import lombok.Getter;

@Getter
public class CustomServerException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
