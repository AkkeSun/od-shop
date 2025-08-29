package com.order.applicatoin.port.in.command;

import lombok.Builder;

@Builder
public record FindSoldProductsCommand(
    Long sellerId,
    Integer page,
    Integer size,
    String searchType,
    Long query
) {
}
