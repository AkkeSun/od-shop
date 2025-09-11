package com.order.applicatoin.port.in.command;

import lombok.Builder;

@Builder
public record ExistsCustomerOrderCommand(
    Long customerId,
    Long productId
) {

}
