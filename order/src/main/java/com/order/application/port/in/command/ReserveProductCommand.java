package com.order.application.port.in.command;

import java.util.List;
import lombok.Builder;

@Builder
public record ReserveProductCommand(
    Long accountId,
    List<ReserveProductCommandItem> items
) {

    @Builder
    public record ReserveProductCommandItem(
        Long productId,
        Long quantity
    ) {

    }
}
