package com.order.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // status code 404 (2001 - 2099) : Not found error
    DoesNotExist_Order(2001, "조회된 주문 정보가 없습니다"),


    // status code 401 (3001 - 3099) : Unauthorized
    INVALID_ACCESS_TOKEN(3001, "유효한 인증 토큰이 아닙니다"),
    INVALID_REFRESH_TOKEN(3002, "유효한 리프레시 토큰이 아닙니다"),
    INVALID_ACCESS_TOKEN_BY_SECURITY(3003, "유효한 인증 토큰이 아닙니다"),

    // status code 422 (4001 - 4099)
    Business_GRPC_RESPONSE_ERROR(4001, ""),
    Business_ALREADY_CANCEL_ORDCER(4002, "이미 취소처리된 상품 입니다"),
    Business_NO_CUSTOMER(4003, "상품을 구매한 사용자만 요청을 처리할 수 있습니다"),


    // status code 403 (5001 - 5099)
    ACCESS_DENIED(5001, "접근권한이 없습니다"),
    ACCESS_DENIED_BY_SECURITY(5002, "접근권한이 없습니다"),

    ;
    private final int code;
    private final String message;
}