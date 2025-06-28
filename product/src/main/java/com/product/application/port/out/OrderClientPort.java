package com.product.application.port.out;

import java.util.List;

public interface OrderClientPort {

    boolean isOrderValid(Long productId, Long accountId);

    List<Long> findProductIdByAccountId(Long accountId, int limit);
}
