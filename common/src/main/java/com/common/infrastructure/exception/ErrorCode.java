package com.common.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // code xx99 == filer/interceptor level error

    // status code 400 (1001): bad request
    INVALID_ROLE(1001, "유효하지 않은 권한 입니다"),

    // status code 404 (2001 - 2099) : Not found error
    DoesNotExist_ACCOUNT_INFO(2001, "조회된 사용자 정보가 없습니다"),
    DoesNotExist_PRODUCT_INFO(2002, "조회된 상픔 정보가 없습니다"),
    DoesNotExist_PRODUCT_RESERVE_INFO(2003, "조회된 상픔 예약 정보가 없습니다"),
    DoesNotExist_Order(2004, "조회된 주문 정보가 없습니다"),


    // status code 401 (3001 - 3099) : Unauthorized
    INVALID_ACCESS_TOKEN(3001, "유효한 인증 토큰이 아닙니다"),
    INVALID_REFRESH_TOKEN(3002, "유효한 리프레시 토큰이 아닙니다"),
    INVALID_ACCESS_TOKEN_BY_SECURITY(3099, "유효한 인증 토큰이 아닙니다"),

    // status code 500 (4001 - 4099)
    Business_SAVED_ACCOUNT_INFO(4001, "등록된 사용자 정보 입니다"),
    Business_OUT_OF_STOCK(4002, "상품 재고가 부족합니다"),
    Business_ES_PRODUCT_SAVE(4003, "엘라스틱서치 상품 등록중 오류가 발생했습니다"),
    Business_SAVED_REVIEW_INFO(4004, "이미 리뷰 등록을 완료 했습니다"),
    Business_DoesNotExist_UPDATE_INFO(4005, "업데이트할 정보가 없습니다"),
    Business_DISTRIBUTE_LOCK(4006, "레디스 분산락 오류가 발생 하였습니다"),
    Business_ALREADY_CANCEL_ORDCER(4007, "이미 취소처리된 상품 입니다"),
    Business_NO_CUSTOMER(4008, "상품을 구매한 사용자만 요청을 처리할 수 있습니다"),
    Business_GRPC_RESPONSE_ERROR(4009, ""),

    // status code 403 (5001 - 5099)
    ACCESS_DENIED(5001, "접근권한이 없습니다"),
    ACCESS_DENIED_BY_SECURITY(5002, "접근권한이 없습니다"),

    ;

    private final int code;
    private final String message;
}