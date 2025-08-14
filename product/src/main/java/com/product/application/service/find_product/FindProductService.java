package com.product.application.service.find_product;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.in.command.FindProductCommand;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductClickLog;
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

    @Override
    public FindProductServiceResponse findProduct(FindProductCommand command) {
        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");

        if (command.isApiCall()) {
            ProductClickLog productClickLog = ProductClickLog.of(command.productId());
            messageProducerPort.sendMessage(clickTopic, toJsonString(productClickLog));
        }
        return FindProductServiceResponse.of(product);
    }
}
