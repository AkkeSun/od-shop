package com.order.adapter.out.persistence.jpa;

import com.order.domain.model.OrderProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "ORDER_PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
class OrderProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_NUMBER")
    private Long orderNumber;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "SELLER_ID")
    private Long sellerId;

    @Column(name = "BUY_QUANTITY")
    private long buyQuantity;

    @Column(name = "BUY_STATUS")
    private String buyStatus;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Column(name = "UPD_DATE_TIME")
    private LocalDateTime updateDateTime;

    static OrderProductEntity of(OrderProduct domain, OrderEntity entity) {
        return OrderProductEntity.builder()
            .orderNumber(entity.getOrderNumber())
            .productId(domain.getProductId())
            .sellerId(domain.getSellerId())
            .buyQuantity(domain.getBuyQuantity())
            .buyStatus(domain.getBuyStatus())
            .regDateTime(entity.getRegDateTime())
            .updateDateTime(domain.getUpdateDateTime())
            .build();
    }

    static OrderProductEntity of(OrderProduct domain) {
        return OrderProductEntity.builder()
            .orderNumber(domain.getOrderNumber())
            .productId(domain.getProductId())
            .sellerId(domain.getSellerId())
            .buyQuantity(domain.getBuyQuantity())
            .buyStatus(domain.getBuyStatus())
            .regDateTime(domain.getRegDateTime())
            .updateDateTime(domain.getUpdateDateTime())
            .build();
    }

    OrderProduct toDomain() {
        return OrderProduct.builder()
            .id(id)
            .orderNumber(orderNumber)
            .productId(productId)
            .sellerId(sellerId)
            .buyQuantity(buyQuantity)
            .buyStatus(buyStatus)
            .regDateTime(regDateTime)
            .updateDateTime(updateDateTime)
            .build();
    }
}
