package com.product.application.service.delete_product;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.DeleteProductUseCase;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductEsStoragePort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Account;
import com.product.domain.model.Product;
import com.product.domain.model.ProductHistory;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteProductService implements DeleteProductUseCase {

    @Value("${kafka.topic.history}")
    private String historyTopic;
    private final ProductStoragePort productStoragePort;
    private final MessageProducerPort messageProducerPort;
    private final ProductEsStoragePort productEsStoragePort;

    @Override
    public DeleteProductServiceResponse deleteProduct(Long productId, Account account) {
        Product product = productStoragePort.findById(productId);
        if (!product.isSeller(account.id())) {
            throw new CustomAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        // TODO: review 삭제
        LocalDateTime deleteAt = LocalDateTime.now();
        productStoragePort.softDeleteById(productId, deleteAt);
        productEsStoragePort.deleteById(productId);

        ProductHistory history = ProductHistory.createProductHistoryForDelete(product, deleteAt);
        messageProducerPort.sendMessage(historyTopic, toJsonString(history));

        return DeleteProductServiceResponse.builder()
                .result(true)
                .build();
    }
}
