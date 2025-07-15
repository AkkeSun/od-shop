package com.productagent.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // status code 404 (2001 - 2099) : Not found error
    DoesNotExist_PROUCT_INFO(2002, "조회된 상픔 정보가 없습니다"),

    ;
    private final int code;
    private final String message;
}