package com.order.adapter.in.controller.find_sold_products;

import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.domain.model.Account;
import com.order.infrastructure.validation.ValidSearchType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindSoldProductsRequest {

    @ValidSearchType
    private String searchType;
    private Integer page;
    private Integer size;
    private String query;

    @Builder
    FindSoldProductsRequest(String searchType, Integer page, Integer size, String query) {
        this.searchType = searchType;
        this.page = page;
        this.size = size;
        this.query = query;
    }

    FindSoldProductsCommand toCommand(Account account) {
        return FindSoldProductsCommand.builder()
            .page(page == null ? 0 : page)
            .size(size == null ? 10 : size)
            .searchType(searchType == null ? "" : searchType)
            .query(query == null ? "" : query)
            .sellerId(account.id())
            .build();
    }
}

