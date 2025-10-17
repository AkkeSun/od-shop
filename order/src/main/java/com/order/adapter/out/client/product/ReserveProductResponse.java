package com.order.adapter.out.client.product;

import lombok.Builder;

@Builder
public record ReserveProductResponse(
    String result,
    Long reserveId,
    String errorMsg
) {

    static ReserveProductResponse ofSuccess(Long reserveId) {
        return ReserveProductResponse.builder()
            .result("Y")
            .reserveId(reserveId)
            .build();
    }

    static ReserveProductResponse ofFailed(String errorMsg) {
        return ReserveProductResponse.builder()
            .result("N")
            .errorMsg(errorMsg)
            .build();
    }

    public boolean isSuccess() {
        return result.equals("Y");
    }
}
