package com.productagent.domain.model;

import com.common.infrastructure.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Product {

    private Long id;
    private Long sellerId;
    private String sellerEmail;
    private String productName;
    private String productImgUrl;
    private String descriptionImgUrl;
    private Set<String> keywords;
    private long price;
    private long quantity;
    private Category category;
    private String deleteYn;
    private LocalDate regDate;
    private LocalDateTime regDateTime;
    private LocalDateTime updateDateTime;

    // product metric
    private long salesCount;
    private long reviewCount;
    private long hitCount;
    private double reviewScore;
    private double totalScore;
    private boolean needsEsUpdate;

    @Builder
    public Product(Category category, String deleteYn, String descriptionImgUrl, long hitCount,
        Long id, Set<String> keywords, boolean needsEsUpdate, long price, String productImgUrl,
        String productName, long quantity, LocalDate regDate,
        LocalDateTime regDateTime, long reviewCount, double reviewScore, long salesCount,
        String sellerEmail, Long sellerId, double totalScore, LocalDateTime updateDateTime) {
        this.category = category;
        this.deleteYn = deleteYn;
        this.descriptionImgUrl = descriptionImgUrl;
        this.hitCount = hitCount;
        this.id = id;
        this.keywords = keywords;
        this.needsEsUpdate = needsEsUpdate;
        this.price = price;
        this.productImgUrl = productImgUrl;
        this.productName = productName;
        this.quantity = quantity;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
        this.salesCount = salesCount;
        this.sellerEmail = sellerEmail;
        this.sellerId = sellerId;
        this.totalScore = totalScore;
        this.updateDateTime = updateDateTime;
    }

    public void updateHitCount() {
        hitCount++;
    }

    public void updateReviewInfo(Review review) {
        reviewCount++;
        reviewScore = (reviewScore + review.score()) / reviewCount;
    }

    public void updateSalesCount() {
        salesCount++;
    }

    public void updateTotalScore() {
        double maxSales = 10000.0;
        double maxReviews = 500.0;
        double maxHits = 10000.0;

        double salesScore = Math.min(salesCount / maxSales, 1.0);
        double reviewCntScore = Math.min(reviewCount / maxReviews, 1.0);
        double hitScore = Math.min(hitCount / maxHits, 1.0);
        double reviewScoreNormalized = reviewScore / 5.0;

        totalScore = (salesScore * 0.4 + reviewCntScore * 0.2 + hitScore * 0.1 +
            reviewScoreNormalized * 0.3) * 100.0;

        needsEsUpdate = true;
        updateDateTime = DateUtil.getCurrentLocalDateTime();
    }

    @JsonIgnore
    public String getEmbeddingDocument() {
        return String.format("""
                    이 상품의 이름은 %s 이고, %s 카테고리에 속해 있습니다. \s
                    상품의 가격은 약 %d원입니다. \s
                    상품과 관련된 키워드는 %s 입니다.
                """,
            productName, category.description(), price, String.join(", ", keywords)
        );
    }

    public void updateNeedsEsUpdate(boolean needsEsUpdate) {
        this.needsEsUpdate = needsEsUpdate;
    }

}
