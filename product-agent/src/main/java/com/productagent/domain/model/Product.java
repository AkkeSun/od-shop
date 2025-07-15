package com.productagent.domain.model;

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
    private Set<String> productOption;
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
        Long id,
        Set<String> keywords, boolean needsEsUpdate, long price, String productImgUrl,
        String productName, Set<String> productOption, long quantity, LocalDate regDate,
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
        this.productOption = productOption;
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
        needsEsUpdate = true;
        updateDateTime = LocalDateTime.now();
    }
}
