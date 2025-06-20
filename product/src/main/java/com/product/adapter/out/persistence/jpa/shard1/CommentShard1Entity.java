package com.product.adapter.out.persistence.jpa.shard1;

import com.product.domain.model.Comment;
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
@Table(name = "COMMENT")
@NoArgsConstructor
class CommentShard1Entity {

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

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "REG_DATE")
    private LocalDate regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    CommentShard1Entity(String comment, String customerEmail, Long customerId, Long id,
        Long productId, LocalDate regDate, LocalDateTime regDateTime, double score) {
        this.comment = comment;
        this.customerEmail = customerEmail;
        this.customerId = customerId;
        this.id = id;
        this.productId = productId;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
        this.score = score;
    }

    static CommentShard1Entity of(Comment comment) {
        return CommentShard1Entity.builder()
            .id(comment.id())
            .productId(comment.productId())
            .customerId(comment.customerId())
            .customerEmail(comment.customerEmail())
            .score(comment.score())
            .comment(comment.comment())
            .regDate(comment.regDate())
            .regDateTime(comment.regDateTime())
            .build();
    }

    Comment toDomain() {
        return Comment.builder()
            .id(id)
            .productId(productId)
            .customerId(customerId)
            .customerEmail(customerEmail)
            .score(score)
            .comment(comment)
            .regDate(regDate)
            .regDateTime(regDateTime)
            .build();
    }
}
