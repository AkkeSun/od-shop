package com.order.adapter.out.persistence.jpa;

import com.order.domain.model.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "`ORDER`")
@NoArgsConstructor
class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_NUMBER")
    private Long orderNumber;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "TOTAL_PRICE")
    private int totalPrice;

    @Column(name = "RECEIVER_NAME")
    private String receiverName;

    @Column(name = "RECEIVER_TEL")
    private String receiverTel;

    @Column(name = "RECEIVER_ADDRESS")
    private String receiverAddress;

    @Column(name = "BUY_STATUS")
    private String buyStatus;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_NUMBER")
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    OrderEntity(Long orderNumber, Long customerId, int totalPrice, String receiverName,
        String receiverTel, String receiverAddress, String buyStatus,
        List<OrderProductEntity> orderProducts, LocalDateTime regDateTime) {
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.receiverName = receiverName;
        this.receiverTel = receiverTel;
        this.receiverAddress = receiverAddress;
        this.buyStatus = buyStatus;
        this.orderProducts = orderProducts;
        this.regDateTime = regDateTime;
    }

    static OrderEntity of(Order order) {
        return OrderEntity.builder()
            .orderNumber(order.orderNumber())
            .customerId(order.customerId())
            .totalPrice(order.totalPrice())
            .receiverName(order.receiverName())
            .receiverTel(order.receiverTel())
            .receiverAddress(order.receiverAddress())
            .buyStatus(order.buyStatus())
            .regDateTime(order.regDateTime())
            .build();
    }

    Order toDomain(List<Long> productIds) {
        return Order.builder()
            .orderNumber(orderNumber)
            .customerId(customerId)
            .totalPrice(totalPrice)
            .receiverName(receiverName)
            .receiverTel(receiverTel)
            .receiverAddress(receiverAddress)
            .buyStatus(buyStatus)
            .productIds(productIds)
            .build();
    }

    Order toDomain() {
        return Order.builder()
            .orderNumber(orderNumber)
            .customerId(customerId)
            .totalPrice(totalPrice)
            .receiverName(receiverName)
            .receiverTel(receiverTel)
            .receiverAddress(receiverAddress)
            .buyStatus(buyStatus)
            .productIds(orderProducts.stream()
                .map(OrderProductEntity::getProductId)
                .toList())
            .regDateTime(regDateTime)
            .build();
    }
}
