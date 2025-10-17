package com.order.application.port.in.command;

import lombok.Builder;

@Builder
public record ExistsCustomerOrderCommand(
    Long customerId,
    Long productId
) {

}
