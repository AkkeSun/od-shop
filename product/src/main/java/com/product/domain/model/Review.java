package com.product.domain.model;

import com.product.application.port.in.command.RegisterReviewCommand;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record Review(
    Long id,
    Long productId,
    Long customerId,
    String customerEmail,
    double score,
    String review,
    LocalDate regDate,
    LocalDateTime regDateTime
) {

    public static Review of(RegisterReviewCommand command, Long id) {
        return Review.builder()
            .id(id)
            .productId(command.productId())
            .customerId(command.account().id())
            .customerEmail(command.account().email())
            .score(command.score())
            .review(command.review())
            .regDate(LocalDate.now())
            .regDateTime(LocalDateTime.now())
            .build();
    }
}
