package com.productagent.application.service;

import com.productagent.application.port.in.UpdateProductQuantityUseCase;
import com.productagent.application.port.out.ProductClientPort;
import com.productagent.domain.model.RefundProductMessage;
import com.productagent.infrastructure.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UpdateProductQuantityService implements UpdateProductQuantityUseCase {

    private final ProductClientPort productClientPort;

    @Override
    public void updateQuantity(String payload) {
        RefundProductMessage message = JsonUtil.parseJson(payload, RefundProductMessage.class);
        productClientPort.updateProductQuantity(message);
    }
}
