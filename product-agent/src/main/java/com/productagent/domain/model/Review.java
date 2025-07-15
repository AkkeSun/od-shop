package com.productagent.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record Review(
    Long id,
    Long productId,
    Long sellerId,
    Long customerId,
    String customerEmail,
    double score,
    String review,
    LocalDate regDate,
    LocalDateTime regDateTime
) {

}
