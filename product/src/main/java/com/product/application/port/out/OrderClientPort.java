package com.product.application.port.out;

public interface OrderClientPort {

    boolean isOrderValid(Long productId, Long accountId);
}
