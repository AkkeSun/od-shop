package com.order.adapter.in.controller.reserve_product;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
record ReserveProductRequest(
    @NotNull(message = "상품 아이디는 필수값 입니다")
    Long productId,

    @NotNull(message = "상품 수량은 필수값 입니다")
    Long quantity
) {

}