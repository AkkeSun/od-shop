package com.order.application.port.in.command;

import lombok.Builder;

@Builder
public record FindCustomerOrdersCommand(
    Long customerId,
    Integer page,
    Integer size
) {

}
