package com.product.domain.model;

import com.product.application.port.in.command.RegisterCommentCommand;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record Comment(
    Long id,
    Long productId,
    Long customerId,
    double score,
    String comment,
    LocalDate regDate,
    LocalDateTime regDateTime
) {

    public static Comment of(RegisterCommentCommand command, Long id) {
        return Comment.builder()
            .id(id)
            .productId(command.productId())
            .customerId(command.account().id())
            .score(command.score())
            .comment(command.comment())
            .regDate(LocalDate.now())
            .regDateTime(LocalDateTime.now())
            .build();
    }
}
