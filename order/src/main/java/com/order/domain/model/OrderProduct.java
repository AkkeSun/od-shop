package com.order.domain.model;

import grpc.product.ConfirmProductReservationResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {

    private Long id;
    private Long orderNumber;
    private Long productId;
    private Long customerId;
    private Long sellerId;
    private long buyQuantity;
    private String buyStatus;
    private LocalDateTime regDateTime;
    private LocalDateTime updateDateTime;

    public static OrderProduct of(ConfirmProductReservationResponse serviceResponse) {
        return OrderProduct.builder()
            .productId(serviceResponse.getProductId())
            .sellerId(serviceResponse.getSellerId())
            .buyQuantity(serviceResponse.getBuyQuantity())
            .buyStatus("ORDER")
            .build();
    }

    public void updateCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void cancel(LocalDateTime now) {
        this.buyStatus = "CANCEL";
        this.updateDateTime = now;
    }
}
