package com.product.application.service.Increase_product_quantity;

import static com.common.infrastructure.exception.ErrorCode.ACCESS_DENIED;
import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.exception.CustomAuthorizationException;
import com.product.application.port.in.IncreaseProductQuantityUseCase;
import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductHistory;
import com.product.infrastructure.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncreaseProductQuantityService implements IncreaseProductQuantityUseCase {

    @Value("${kafka.topic.history}")
    private String historyTopic;
    private final ProductStoragePort productStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    @DistributedLock(key = "PRODUCT_QUANTITY", isUniqueKey = true)
    public IncreaseProductQuantityServiceResponse update(
        Long productId, IncreaseProductQuantityCommand command
    ) {
        Product product = productStoragePort.findByIdAndDeleteYn(productId, "N");
        if (!command.isRefundRequest() && !product.isSeller(command.loginInfo().getId())) {
            throw new CustomAuthorizationException(ACCESS_DENIED);
        }

        product.increaseQuantity(command);
        productStoragePort.register(product);
        
        messageProducerPort.sendMessage(historyTopic, toJsonString(
            ProductHistory.createProductHistoryForUpdateQuantity(product, command.quantity())));
        log.error(String.valueOf(product.getQuantity()));
        return IncreaseProductQuantityServiceResponse.ofSuccess();
    }
}
