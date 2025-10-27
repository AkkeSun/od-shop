package com.productagent.fakeClass;

import com.productagent.application.port.out.ProductClientPort;
import com.productagent.domain.model.RefundProductMessage;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeProductClientPort implements ProductClientPort {

    public List<RefundProductMessage> updateQuantityRequests = new ArrayList<>();
    public boolean shouldThrowException = false;

    @Override
    public void updateProductQuantity(RefundProductMessage message) {
        if (shouldThrowException) {
            throw new RuntimeException("Simulated exception");
        }

        updateQuantityRequests.add(message);
        log.info("FakeProductClientPort updated quantity: productId={}, quantity={}",
            message.productId(), message.quantity());
    }
}
