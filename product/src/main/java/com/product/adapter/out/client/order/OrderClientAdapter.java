package com.product.adapter.out.client.order;

import com.product.application.port.out.OrderClientPort;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class OrderClientAdapter implements OrderClientPort {

    // TODO
    @Override
    public boolean isOrderValid(Long productId, Long accountId) {
        return true;
    }

    @Override
    public List<Long> findProductIdByAccountId(Long accountId, int limit) {
        return List.of(
            1937295956458344448L,
            1935511950012190720L
        );
    }

}
