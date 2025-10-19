package com.product.application.service.register_product;


import static com.common.infrastructure.exception.ErrorCode.Business_ES_PRODUCT_SAVE;

import com.common.infrastructure.exception.CustomBusinessException;
import com.common.infrastructure.util.SnowflakeGenerator;
import com.product.application.port.in.RegisterProductUseCase;
import com.product.application.port.in.command.RegisterProductCommand;
import com.product.application.port.out.ElasticSearchClientPort;
import com.product.application.port.out.GeminiClientPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterProductService implements RegisterProductUseCase {

    private final GeminiClientPort geminiClientPort;
    private final SnowflakeGenerator snowflakeGenerator;
    private final ProductStoragePort productStoragePort;
    private final ElasticSearchClientPort elasticSearchClientPort;

    @Override
    public RegisterProductServiceResponse registerProduct(RegisterProductCommand command) {

        Product product = Product.of(command, snowflakeGenerator.nextId());
        String keywords = geminiClientPort.query(product.getKeywordQueryDocument());
        product.updateKeywords(keywords);
        productStoragePort.register(product);

        float[] embedding = geminiClientPort.embedding(product.getEmbeddingDocument());

        // elastic search error transaction rollback
        try {
            elasticSearchClientPort.register(product, embedding);
        } catch (Exception e) {
            log.error("[registerProduct] elasticsearch save error : {}", e.getMessage());
            productStoragePort.deleteById(product.getId());
            throw new CustomBusinessException(Business_ES_PRODUCT_SAVE);
        }

        return RegisterProductServiceResponse.of(product);
    }
}
