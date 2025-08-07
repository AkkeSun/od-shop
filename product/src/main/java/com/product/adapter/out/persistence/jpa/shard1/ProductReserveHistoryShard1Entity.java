package com.product.adapter.out.persistence.jpa.shard1;

import com.product.domain.model.ProductReserveHistory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "PRODUCT_RESERVE_HISTORY")
class ProductReserveHistoryShard1Entity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCT_ID")
    Long productId;

    @Column(name = "CUSTOMER_ID")
    Long customerId;

    @Column(name = "RESERVED_QUANTITY")
    long reservedQuantity;

    @Builder
    ProductReserveHistoryShard1Entity(Long id, Long productId, Long customerId,
        long reservedQuantity) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.reservedQuantity = reservedQuantity;
    }

    static ProductReserveHistoryShard1Entity of(ProductReserveHistory domain) {
        return ProductReserveHistoryShard1Entity.builder()
            .id(domain.id())
            .productId(domain.productId())
            .customerId(domain.customerId())
            .reservedQuantity(domain.reservedQuantity())
            .build();
    }

    ProductReserveHistory toDomain() {
        return ProductReserveHistory.builder()
            .id(id)
            .productId(productId)
            .customerId(customerId)
            .reservedQuantity(reservedQuantity)
            .build();

    }
}
