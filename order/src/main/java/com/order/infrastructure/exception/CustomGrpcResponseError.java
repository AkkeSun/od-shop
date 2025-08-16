package com.order.infrastructure.exception;

import lombok.Getter;

@Getter
public class CustomGrpcResponseError extends RuntimeException {

    private final String errorMessage;
    
    public CustomGrpcResponseError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
