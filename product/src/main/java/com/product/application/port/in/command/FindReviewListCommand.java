package com.product.application.port.in.command;

import lombok.Builder;
import org.springframework.data.domain.PageRequest;

@Builder
public record FindReviewListCommand(
    Long productId,
    PageRequest pageable
) {

}
