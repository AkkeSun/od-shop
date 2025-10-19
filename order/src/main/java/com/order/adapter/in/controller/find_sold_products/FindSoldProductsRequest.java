package com.order.adapter.in.controller.find_sold_products;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.validation.Contains;
import com.order.application.port.in.command.FindSoldProductsCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FindSoldProductsRequest {

    @Contains(values = {"customerId", "productId", "buyStatus"}, message = "유효하지 않은 검색 조건 입니다")
    private String searchType;
    private Integer page;
    private Integer size;
    private String query;

    FindSoldProductsCommand toCommand(LoginAccountInfo loginInfo) {
        return FindSoldProductsCommand.builder()
            .page(page == null ? 0 : page)
            .size(size == null ? 10 : size)
            .searchType(searchType == null ? "" : searchType)
            .query(query == null ? "" : query)
            .sellerId(loginInfo.getId())
            .build();
    }
}

