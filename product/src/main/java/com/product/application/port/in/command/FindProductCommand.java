package com.product.application.port.in.command;

import lombok.Builder;

@Builder
public record FindProductCommand(
    Long productId,
    boolean isApiCall
) {

    public static FindProductCommand ofApiCall(Long productId) {
        return FindProductCommand.builder()
            .productId(productId)
            .isApiCall(true)
            .build();
    }

    public static FindProductCommand ofGrpcCall(Long productId) {
        return FindProductCommand.builder()
            .productId(productId)
            .isApiCall(false)
            .build();
    }
}
