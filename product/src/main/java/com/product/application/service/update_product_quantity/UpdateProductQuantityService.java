package com.product.application.service.update_product_quantity;

import static com.product.infrastructure.exception.ErrorCode.ACCESS_DENIED;
import static com.product.infrastructure.exception.ErrorCode.Business_OUT_OF_STOCK;
import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.UpdateProductQuantityUseCase;
import com.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductHistory;
import com.product.infrastructure.aop.DistributedLock;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.CustomBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UpdateProductQuantityService implements UpdateProductQuantityUseCase {

    @Value("${kafka.topic.history}")
    private String historyTopic;
    private final ProductStoragePort productStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    @DistributedLock(key = "PRODUCT_QUANTITY", isUniqueKey = true)
    public UpdateProductQuantityServiceResponse updateProductQuantity(
        UpdateProductQuantityCommand command) {

        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");
        product.updateQuantity(command);

        if (command.isPurchase() && !product.isAvailableForSale()) {
            throw new CustomBusinessException(Business_OUT_OF_STOCK);

        } else if (command.isAddQuantity()) {
            if (!product.isSeller(command.account().id())) {
                throw new CustomAuthorizationException(ACCESS_DENIED);
            }
            messageProducerPort.sendMessage(historyTopic, toJsonString(
                ProductHistory.createProductHistoryForUpdateQuantity(product, command.quantity())));
        }

        productStoragePort.register(product);
        return UpdateProductQuantityServiceResponse.builder()
            .result(true)
            .build();
    }
}
