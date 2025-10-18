package com.productagent.application.service;

import static com.common.infrastructure.util.JsonUtil.parseJson;

import com.productagent.application.port.in.UpdateProductQuantityUseCase;
import com.productagent.application.port.out.ProductClientPort;
import com.productagent.domain.model.RefundProductMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UpdateProductQuantityService implements UpdateProductQuantityUseCase {

    private final ProductClientPort productClientPort;

    @Override
    public void updateQuantity(String payload) {
        RefundProductMessage message = parseJson(payload, RefundProductMessage.class);
        productClientPort.updateProductQuantity(message);
    }
}
