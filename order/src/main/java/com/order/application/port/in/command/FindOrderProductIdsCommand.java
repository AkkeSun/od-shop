package com.order.application.port.in.command;

import lombok.Builder;

@Builder
public record FindOrderProductIdsCommand(
    Long customerId,
    int limit
) {

}
