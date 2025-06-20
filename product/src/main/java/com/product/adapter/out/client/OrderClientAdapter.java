package com.product.adapter.out.client;

import com.product.application.port.out.OrderClientPort;
import org.springframework.stereotype.Component;

@Component
class OrderClientAdapter implements OrderClientPort {

    // TODO
    @Override
    public boolean isOrderValid(Long productId, Long accountId) {
        return true;
    }

}
