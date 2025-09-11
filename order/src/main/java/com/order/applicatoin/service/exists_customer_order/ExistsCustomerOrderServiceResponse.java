package com.order.applicatoin.service.exists_customer_order;

import lombok.Builder;

@Builder
public record ExistsCustomerOrderServiceResponse(
    boolean exists
) {

}
