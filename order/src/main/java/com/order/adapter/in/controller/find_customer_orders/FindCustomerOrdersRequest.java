package com.order.adapter.in.controller.find_customer_orders;

import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.domain.model.Account;
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

    FindCustomerOrdersCommand toCommand(Account account) {
        return FindCustomerOrdersCommand.builder()
            .page(page == null ? 0 : page)
            .size(size == null ? 10 : size)
            .customerId(account.id())
            .build();
    }
}
