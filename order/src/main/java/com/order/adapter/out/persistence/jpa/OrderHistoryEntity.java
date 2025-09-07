package com.order.adapter.out.persistence.jpa;

import com.order.domain.model.Order;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_HISTORY")
class OrderHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_NUMBER")
    private Long orderNumber;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    static OrderHistoryEntity ofCancel(Order order) {
        return OrderHistoryEntity.builder()
            .orderNumber(order.orderNumber())
            .description("CANCEN")
            .regDateTime(order.products().getFirst().getUpdateDateTime())
            .build();
    }
}
