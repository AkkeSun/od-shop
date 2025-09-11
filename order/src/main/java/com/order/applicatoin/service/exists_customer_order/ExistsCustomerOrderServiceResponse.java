package com.order.applicatoin.service.exists_customer_order;

public record ExistsCustomerOrderServiceResponse(
    boolean exists
) {

    public static ExistsCustomerOrderServiceResponse of(boolean result) {
        return new ExistsCustomerOrderServiceResponse(result);
    }
}
