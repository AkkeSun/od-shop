package com.order.application.service.cancel_order;

public record CancelOrderServiceResponse(
    Boolean result
) {

    public static CancelOrderServiceResponse ofSuccess() {
        return new CancelOrderServiceResponse(true);
    }
}
