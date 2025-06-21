package com.product.fakeClass;

import com.product.application.port.out.OrderClientPort;

public class DummyOrderClientPort implements OrderClientPort {

    @Override
    public boolean isOrderValid(Long productId, Long accountId) {
        return accountId.equals(1L);
    }
}
