package com.product.application.service.find_product;

import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindProductService implements FindProductUseCase {

    private final ProductStoragePort productStoragePort;

    @NewSpan
    @Override
    public FindProductServiceResponse findProduct(Long productId) {
        Product product = productStoragePort.findById(productId);
        return FindProductServiceResponse.of(product);
    }
}
