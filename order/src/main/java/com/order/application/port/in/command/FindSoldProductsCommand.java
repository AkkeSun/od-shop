package com.order.application.port.in.command;

import lombok.Builder;

@Builder
public record FindSoldProductsCommand(
    Long sellerId,
    Integer page,
    Integer size,
    String searchType,
    String query
) {
}
