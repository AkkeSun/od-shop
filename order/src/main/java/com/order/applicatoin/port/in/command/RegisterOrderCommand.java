package com.order.applicatoin.port.in.command;

import java.util.List;
import lombok.Builder;

@Builder
public record RegisterOrderCommand(
    Long accountId,
    List<RegisterOrderCommandItem> reserveInfos,
    int totalPrice,
    String receiverName,
    String receiverTel,
    String receiverAddress
) {

    public record RegisterOrderCommandItem(
        Long productId,
        Long reserveId
    ) {

    }
}
