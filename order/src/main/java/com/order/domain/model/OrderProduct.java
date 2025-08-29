package com.order.domain.model;

import grpc.product.ConfirmProductReservationResponse;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderProduct {

    private Long id;
    private Long orderNumber;
    private Long productId;
    private Long customerId;
    private Long sellerId;
    private long buyQuantity;
    private String buyStatus;
    private LocalDateTime regDateTime;

    @Builder
    public OrderProduct(Long id, Long orderNumber, Long productId, Long customerId, Long sellerId,
        long buyQuantity,
        String buyStatus, LocalDateTime regDateTime) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.buyQuantity = buyQuantity;
        this.buyStatus = buyStatus;
        this.regDateTime = regDateTime;
    }

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
}
