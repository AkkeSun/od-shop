package com.product.application.port.in.command;

import com.common.infrastructure.resolver.LoginAccountInfo;
import lombok.Builder;

@Builder
public record IncreaseProductQuantityCommand(
    long quantity,
    LoginAccountInfo loginInfo
) {

    public boolean isRefundRequest() {
        return loginInfo == null;
    }
}
