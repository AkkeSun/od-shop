package com.product.application.service.register_product;


import static com.product.infrastructure.exception.ErrorCode.Business_ES_PRODUCT_SAVE;

import com.product.application.port.in.RegisterProductUseCase;
import com.product.application.port.in.command.RegisterProductCommand;
import com.product.application.port.out.ProductEsStoragePort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.infrastructure.exception.CustomBusinessException;
import com.product.infrastructure.util.EmbeddingUtil;
import com.product.infrastructure.util.SnowflakeGenerator;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterProductService implements RegisterProductUseCase {

    private final EmbeddingUtil embeddingUtil;
    private final SnowflakeGenerator snowflakeGenerator;
    private final ProductStoragePort productStoragePort;
    private final ProductEsStoragePort productEsStoragePort;

    @NewSpan
    @Override
    public RegisterProductServiceResponse registerProduct(RegisterProductCommand command) {

        Product product = Product.of(command, snowflakeGenerator.nextId());
        productStoragePort.register(product);

        float[] embedding = embeddingUtil.embedToFloatArray(product.getEmbeddingDocument());

        // elastic search error transaction rollback
        try {
            productEsStoragePort.register(product, embedding);
        } catch (Exception e) {
            log.error("[registerProduct] elasticsearch save error : {}", e.getMessage());
            productStoragePort.deleteById(product.getId());
            throw new CustomBusinessException(Business_ES_PRODUCT_SAVE);
        }

        return RegisterProductServiceResponse.of(product);
    }
}
