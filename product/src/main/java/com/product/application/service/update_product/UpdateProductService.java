package com.product.application.service.update_product;

import static com.product.infrastructure.exception.ErrorCode.ACCESS_DENIED;
import static com.product.infrastructure.exception.ErrorCode.Business_DoesNotExist_UPDATE_INFO;
import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.UpdateProductUseCase;
import com.product.application.port.in.command.UpdateProductCommand;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductHistory;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.CustomBusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UpdateProductService implements UpdateProductUseCase {

    @Value("${kafka.topic.history}")
    private String historyTopic;
    private final MessageProducerPort messageProducerPort;
    private final ProductStoragePort productStoragePort;

    @Override
    public UpdateProductServiceResponse updateProduct(UpdateProductCommand command) {
        Product product = productStoragePort.findById(command.productId());
        if (!product.isSeller(command.account().id())) {
            throw new CustomAuthorizationException(ACCESS_DENIED);
        }

        List<String> updateList = product.update(command);
        if (updateList.isEmpty()) {
            throw new CustomBusinessException(Business_DoesNotExist_UPDATE_INFO);
        }

        productStoragePort.register(product);
        ProductHistory history = ProductHistory.createProductHistoryForUpdate(product, updateList);
        messageProducerPort.sendMessage(historyTopic, toJsonString(history));
        return UpdateProductServiceResponse.of(product);
    }
}
