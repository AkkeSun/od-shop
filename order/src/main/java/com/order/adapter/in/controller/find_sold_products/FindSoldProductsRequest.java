package com.order.adapter.in.controller.find_sold_products;

import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.domain.model.Account;
import lombok.Builder;

@Builder
record FindSoldProductsRequest(
    Integer page,
    Integer size,
    String searchType,  // todo: sellerId, productId, buyStatus
    Long query
) {

    FindSoldProductsCommand toCommand(Account account) {
        return FindSoldProductsCommand.builder()
            .page(page == null ? 0 : page)
            .size(size == null ? 10 : size)
            .searchType(searchType == null ? "" : searchType)
            .query(query == null ? 0L : query)
            .sellerId(account.id())
            .build();
    }
}
