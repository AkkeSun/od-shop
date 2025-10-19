package com.order.application.port.in.command;

import com.common.infrastructure.resolver.LoginAccountInfo;
import lombok.Builder;

@Builder
public record CancelOrderCommand(
    Long orderId,
    LoginAccountInfo account
) {

}
