package com.productagent.adapter.out.persistence.jpa.shard1;

import com.productagent.domain.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "PRODUCT_METRIC")
@NoArgsConstructor
class ProductMetricShard1Entity {

    @Id
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private ProductShard1Entity product;

    @Column(name = "SALES_COUNT")
    private long salesCount;

    @Column(name = "REVIEW_COUNT")
    private long reviewCount;

    @Column(name = "HIT_COUNT")
    private long hitCount;

    @Column(name = "REVIEW_SCORE")
    private double reviewScore;

    @Column(name = "TOTAL_SCORE")
    private double totalScore;

    @Column(name = "NEEDS_ES_UPDATE")
    private boolean needsEsUpdate;

    @Column(name = "REG_DATE")
    private LocalDate regDate;

    @Column(name = "UPDATE_DATE_TIME")
    private LocalDateTime updateTime;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    ProductMetricShard1Entity(long hitCount, Long id, boolean needsEsUpdate,
        ProductShard1Entity product, LocalDate regDate, LocalDateTime regDateTime, long reviewCount,
        double reviewScore, long salesCount, double totalScore, LocalDateTime updateTime) {
        this.hitCount = hitCount;
        this.id = id;
        this.needsEsUpdate = needsEsUpdate;
        this.product = product;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
        this.salesCount = salesCount;
        this.totalScore = totalScore;
        this.updateTime = updateTime;
    }

    static ProductMetricShard1Entity of(Product product) {
        return ProductMetricShard1Entity.builder()
            .id(product.getId())
            .product(ProductShard1Entity.of(product))
            .hitCount(product.getHitCount())
            .reviewCount(product.getReviewCount())
            .reviewScore(product.getReviewScore())
            .salesCount(product.getSalesCount())
            .totalScore(product.getTotalScore())
            .needsEsUpdate(product.isNeedsEsUpdate())
            .regDate(product.getRegDate())
            .regDateTime(product.getRegDateTime())
            .updateTime(product.getUpdateDateTime())
            .build();
    }

    Product toDomain() {
        return Product.builder()
            .id(id)
            .sellerId(product.getSellerId())
            .sellerEmail(product.getSellerEmail())
            .productName(product.getProductName())
            .productImgUrl(product.getProductImgUrl())
            .productOption(new HashSet<>(List.of(product.getProductOption().split(","))))
            .keywords(new HashSet<>(List.of(product.getKeyword().split(","))))
            .descriptionImgUrl(product.getDescriptionImgUrl())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .category(product.getCategory())
            .deleteYn(product.getDeleteYn())
            .regDate(regDate)
            .regDateTime(regDateTime)
            .updateDateTime(product.getUpdateDateTime())
            .salesCount(salesCount)
            .reviewCount(reviewCount)
            .hitCount(hitCount)
            .reviewScore(reviewScore)
            .totalScore(totalScore)
            .needsEsUpdate(needsEsUpdate)
            .build();
    }
}
