package com.product.application.service.reserve_product;

import com.product.domain.model.ProductReserveHistory;
import lombok.Builder;

@Builder
public record ReserveProductServiceResponse(
    Long reserveId
) {

    public static ReserveProductServiceResponse of(ProductReserveHistory history) {
        return ReserveProductServiceResponse.builder()
            .reserveId(history.id())
            .build();
    }
}
