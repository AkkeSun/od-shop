package com.order.adapter.in.controller.reserve_product;

import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
record ReserveProductRequest(
    @NotNull(message = "상품 아이디는 필수값 입니다")
    Long productId,

    @NotNull(message = "상품 수량은 필수값 입니다")
    Long quantity
) {

    ReserveProductCommandItem toCommandItem() {
        return ReserveProductCommandItem.builder()
            .productId(productId)
            .quantity(quantity)
            .build();
    }
}