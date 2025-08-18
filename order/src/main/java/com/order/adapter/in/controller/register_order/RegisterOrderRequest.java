package com.order.adapter.in.controller.register_order;

import java.util.List;
import lombok.Builder;

@Builder
public record RegisterOrderRequest(
    List<Long> reserveIds,
    int totalPrice,
    String receiverName,
    String receiverTel,
    String receiverAddress

) {

}
