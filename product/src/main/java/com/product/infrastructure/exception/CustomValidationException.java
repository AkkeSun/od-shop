package com.product.infrastructure.exception;

import lombok.Getter;

@Getter
public class CustomValidationException extends RuntimeException {

    private final String errorMessage;

    public CustomValidationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
