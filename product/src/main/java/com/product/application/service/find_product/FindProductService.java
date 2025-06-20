package com.product.application.service.find_product;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductClickLog;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindProductService implements FindProductUseCase {

    @Value("${kafka.topic.click}")
    private String clickTopic;
    private final ProductStoragePort productStoragePort;
    private final MessageProducerPort messageProducerPort;

    @NewSpan
    @Override
    public FindProductServiceResponse findProduct(Long productId) {
        Product product = productStoragePort.findById(productId);

        ProductClickLog productClickLog = ProductClickLog.of(productId);
        messageProducerPort.sendMessage(clickTopic, toJsonString(productClickLog));
        return FindProductServiceResponse.of(product);
    }
}
