package com.product.fakeClass;

import com.product.application.port.out.OrderClientPort;
import java.util.Collections;
import java.util.List;

public class DummyOrderClientPort implements OrderClientPort {

    @Override
    public boolean isOrderValid(Long productId, Long accountId) {
        return accountId.equals(1L);
    }

    @Override
    public List<Long> findProductIdByAccountId(Long accountId, int limit) {
        if (accountId.equals(10L)) {
            return List.of(11L);
        }
        return Collections.emptyList();
    }
}
