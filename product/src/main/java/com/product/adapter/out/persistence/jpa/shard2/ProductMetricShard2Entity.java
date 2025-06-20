package com.product.adapter.out.persistence.jpa.shard2;

import com.product.domain.model.Product;
import com.product.domain.model.ProductMetric;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "PRODUCT_METRIC")
@NoArgsConstructor
class ProductMetricShard2Entity {

    @Id
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private ProductShard2Entity product;

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
    ProductMetricShard2Entity(long hitCount, Long id, boolean needsEsUpdate,
        ProductShard2Entity product, LocalDate regDate, LocalDateTime regDateTime, long reviewCount,
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

    static ProductMetricShard2Entity of(Product product) {
        return ProductMetricShard2Entity.builder()
            .id(product.getId())
            .product(ProductShard2Entity.of(product))
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

    ProductMetric toDomain() {
        return ProductMetric.builder()
            .metricId(this.id)
            .product(this.product.toDomain())
            .hitCount(this.hitCount)
            .reviewCount(this.reviewCount)
            .reviewScore(this.reviewScore)
            .salesCount(this.salesCount)
            .totalScore(this.totalScore)
            .regDate(this.regDate)
            .regDateTime(this.regDateTime)
            .updateTime(this.updateTime)
            .build();
    }
}
