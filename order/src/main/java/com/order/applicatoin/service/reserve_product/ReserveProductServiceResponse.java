package com.order.applicatoin.service.reserve_product;

import lombok.Builder;

@Builder
public record ReserveProductServiceResponse(
    Long productId,
    Long reserveId
) {
}
