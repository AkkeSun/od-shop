package com.order.adapter.in.controller.find_customer_orders;

import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.domain.model.Account;
import lombok.Builder;

@Builder
record FindCustomerOrdersRequest(
    Integer page,
    Integer size
) {

    FindCustomerOrdersCommand toCommand(Account account) {
        return FindCustomerOrdersCommand.builder()
            .page(page == null ? 0 : page)
            .size(size == null ? 10 : size)
            .customerId(account.id())
            .build();
    }
}
