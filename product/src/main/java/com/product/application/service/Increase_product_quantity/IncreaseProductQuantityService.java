package com.product.application.service.Increase_product_quantity;

import static com.product.infrastructure.exception.ErrorCode.ACCESS_DENIED;
import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.IncreaseProductQuantityUseCase;
import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductHistory;
import com.product.infrastructure.aop.DistributedLock;
import com.product.infrastructure.exception.CustomAuthorizationException;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncreaseProductQuantityService implements IncreaseProductQuantityUseCase {

    @Value("${kafka.topic.history}")
    private String historyTopic;
    private final ProductStoragePort productStoragePort;
    private final MessageProducerPort messageProducerPort;

    @NewSpan
    @Override
    @DistributedLock(key = "PRODUCT_QUANTITY", isUniqueKey = true)
    public IncreaseProductQuantityServiceResponse update(IncreaseProductQuantityCommand command) {
        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");
        if (!product.isSeller(command.account().id())) {
            throw new CustomAuthorizationException(ACCESS_DENIED);
        }

        product.increaseQuantity(command);
        productStoragePort.register(product);
        
        messageProducerPort.sendMessage(historyTopic, toJsonString(
            ProductHistory.createProductHistoryForUpdateQuantity(product, command.quantity())));
        return IncreaseProductQuantityServiceResponse.ofSuccess();
    }
}
