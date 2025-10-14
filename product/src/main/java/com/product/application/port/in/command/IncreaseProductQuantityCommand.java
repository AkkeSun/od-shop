package com.product.application.port.in.command;

import com.product.domain.model.Account;
import lombok.Builder;

@Builder
public record IncreaseProductQuantityCommand(
    long quantity,
    Account account
) {

    public boolean isRefundRequest() {
        return account == null;
    }
}
