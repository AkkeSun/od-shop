package com.product.application.service.delete_product;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.DeleteProductUseCase;
import com.product.application.port.out.ElasticSearchClientPort;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.application.port.out.ReviewStoragePort;
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
    private final ReviewStoragePort reviewStoragePort;
    private final ProductStoragePort productStoragePort;
    private final MessageProducerPort messageProducerPort;
    private final ElasticSearchClientPort elasticSearchClientPort;

    @Override
    public DeleteProductServiceResponse deleteProduct(Long productId, Account account) {
        Product product = productStoragePort.findByIdAndDeleteYn(productId, "N");
        if (!product.isSeller(account.id())) {
            throw new CustomAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        LocalDateTime deleteAt = LocalDateTime.now();
        reviewStoragePort.deleteByProductId(productId);
        productStoragePort.softDeleteById(productId, deleteAt);
        elasticSearchClientPort.deleteById(productId);

        ProductHistory history = ProductHistory.createProductHistoryForDelete(product, deleteAt);
        messageProducerPort.sendMessage(historyTopic, toJsonString(history));

        return DeleteProductServiceResponse.builder()
            .result(true)
            .build();
    }
}
