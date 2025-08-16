package com.order.applicatoin.service.reserve_product;

import java.util.List;
import lombok.Builder;

@Builder
public record ReserveProductServiceResponse(
    List<Long> reservationIds
) {

    static ReserveProductServiceResponse of(List<Long> reservationIds) {
        return new ReserveProductServiceResponse(reservationIds);
    }
}
