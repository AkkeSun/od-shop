package com.product.application.service.reserve_product;

import com.product.domain.model.ProductReserveHistory;
import grpc.product.ReserveProductStubResponse;
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

    public ReserveProductStubResponse toStubResponse() {
        return ReserveProductStubResponse.newBuilder()
            .setReserveId(reserveId)
            .build();
    }
}
