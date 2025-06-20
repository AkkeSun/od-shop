package com.product.application.port.in.command;

import lombok.Builder;

@Builder
public record FindCommentListCommand(
    Long productId,
    int page,
    int size
) {

}
