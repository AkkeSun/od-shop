package com.order.applicatoin.port.in.command;

import lombok.Builder;

@Builder
public record FindOrderProductIdsCommand(
    Long customerId,
    int limit
) {

}
