package com.order.adapter.in.controller.find_customer_orders;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.order.application.port.in.command.FindCustomerOrdersCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FindCustomerOrdersRequest {

    private Integer page;
    private Integer size;

    FindCustomerOrdersCommand toCommand(LoginAccountInfo loginInfo) {
        return FindCustomerOrdersCommand.builder()
            .page(page == null ? 0 : page)
            .size(size == null ? 10 : size)
            .customerId(loginInfo.getId())
            .build();
    }
}
