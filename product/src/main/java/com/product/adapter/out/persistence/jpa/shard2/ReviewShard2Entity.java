package com.product.adapter.out.persistence.jpa.shard2;

import com.product.domain.model.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "REVIEW")
@NoArgsConstructor
class ReviewShard2Entity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;

    @Column(name = "SCORE")
    private double score;

    @Column(name = "REVIEW")
    private String review;

    @Column(name = "REG_DATE")
    private LocalDate regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    ReviewShard2Entity(String review, String customerEmail, Long customerId, Long id,
        Long productId, LocalDate regDate, LocalDateTime regDateTime, double score) {
        this.review = review;
        this.customerEmail = customerEmail;
        this.customerId = customerId;
        this.id = id;
        this.productId = productId;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
        this.score = score;
    }

    static ReviewShard2Entity of(Review review) {
        return ReviewShard2Entity.builder()
            .id(review.id())
            .productId(review.productId())
            .customerId(review.customerId())
            .customerEmail(review.customerEmail())
            .score(review.score())
            .review(review.review())
            .regDate(review.regDate())
            .regDateTime(review.regDateTime())
            .build();
    }

    Review toDomain() {
        return Review.builder()
            .id(id)
            .productId(productId)
            .customerId(customerId)
            .customerEmail(customerEmail)
            .score(score)
            .review(review)
            .regDate(regDate)
            .regDateTime(regDateTime)
            .build();
    }
}
