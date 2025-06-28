package com.product.adapter.out.persistence.jpa.shard1;

import com.product.domain.model.ProductRecommend;
import com.product.domain.model.RecommendType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "PRODUCT_RECOMMEND")
@NoArgsConstructor
class RecommendShard1Entity {

    @Id
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private ProductShard1Entity product;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private RecommendType type;

    @Column(name = "CHECK_DATE")
    private LocalDate checkDate;

    @Column(name = "REG_DATE")
    private LocalDate regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    RecommendShard1Entity(LocalDate checkDate, Long id, ProductShard1Entity product,
        LocalDate regDate, LocalDateTime regDateTime, RecommendType type) {
        this.checkDate = checkDate;
        this.id = id;
        this.product = product;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
        this.type = type;
    }

    ProductRecommend toDomain() {
        return ProductRecommend.builder()
            .id(product.getId())
            .type(type)
            .productName(product.getProductName())
            .productImgUrl(product.getSellerEmail())
            .productImgUrl(product.getProductImgUrl())
            .build();
    }
}
