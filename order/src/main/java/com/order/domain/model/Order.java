package com.order.domain.model;

import com.order.applicatoin.port.in.command.RegisterOrderCommand;
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
    List<OrderProduct> products,
    LocalDateTime regDateTime
) {

    public static Order of(RegisterOrderCommand command, List<OrderProduct> products) {
        return Order.builder()
            .customerId(command.accountId())
            .totalPrice(command.totalPrice())
            .receiverName(command.receiverName())
            .receiverTel(command.receiverTel())
            .receiverAddress(command.receiverAddress())
            .products(products)
            .regDateTime(LocalDateTime.now())
            .build();
    }

    public void cancel(LocalDateTime now) {
        products.forEach(order -> order.cancel(now));
    }
}
