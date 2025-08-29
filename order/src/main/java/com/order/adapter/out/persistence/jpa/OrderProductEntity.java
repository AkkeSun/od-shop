package com.order.adapter.out.persistence.jpa;

import com.order.domain.model.OrderProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ORDER_PRODUCT")
@NoArgsConstructor
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

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    public OrderProductEntity(Long id, Long orderNumber, Long productId, Long sellerId,
        LocalDateTime regDateTime) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.sellerId = sellerId;
        this.regDateTime = regDateTime;
    }

    static OrderProductEntity of(OrderProduct domain, OrderEntity entity) {
        return OrderProductEntity.builder()
            .orderNumber(entity.getOrderNumber())
            .productId(domain.id())
            .sellerId(domain.sellerId())
            .regDateTime(entity.getRegDateTime())
            .build();
    }

    public OrderProduct toDomain() {
        return OrderProduct.builder()
            .id(id)
            .orderNumber(orderNumber)
            .productId(productId)
            .sellerId(sellerId)
            .regDateTime(regDateTime)
            .build();
    }
}
