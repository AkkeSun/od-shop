package com.order.application.port.in.command;

import com.order.domain.model.Account;
import lombok.Builder;

@Builder
public record CancelOrderCommand(
    Long orderId,
    Account account
) {

}
