package com.order.domain.model;

import com.order.applicatoin.port.in.RegisterOrderCommand;
import com.order.applicatoin.port.in.RegisterOrderCommand.RegisterOrderCommandItem;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record Order(
    Long orderNumber,
    Long customerId,
    int totalPrice,
    String receiverName,
    String receiverTel,
    String receiverAddress,
    String buyStatus,
    List<Long> productIds,
    LocalDateTime regDateTime
) {

    public static Order of(RegisterOrderCommand command) {
        return Order.builder()
            .customerId(command.accountId())
            .totalPrice(command.totalPrice())
            .receiverName(command.receiverName())
            .receiverTel(command.receiverTel())
            .receiverAddress(command.receiverAddress())
            .buyStatus("ORDER")
            .productIds(command.reserveInfos().stream()
                .map(RegisterOrderCommandItem::productId)
                .toList())
            .regDateTime(LocalDateTime.now())
            .build();
    }
}
