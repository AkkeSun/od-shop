package com.productagent.application.port.out;

import com.productagent.domain.model.RefundProductMessage;

public interface ProductClientPort {

    void updateProductQuantity(RefundProductMessage message);
}
