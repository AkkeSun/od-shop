package com.product.adapter.in.controller.delete_product;

import com.product.application.port.in.DeleteProductUseCase;
import com.product.application.service.delete_product.DeleteProductServiceResponse;
import com.product.domain.model.Account;
import com.product.infrastructure.resolver.LoginAccount;
import com.product.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteProductController {

    private final DeleteProductUseCase deleteProductUseCase;

    @DeleteMapping("/products/{productId}")
    ApiResponse<DeleteProductResponse> deleteProduct(@PathVariable Long productId,
        @LoginAccount Account account) {
        DeleteProductServiceResponse serviceResponse = deleteProductUseCase
            .deleteProduct(productId, account);

        return ApiResponse.ok(DeleteProductResponse.of(serviceResponse));
    }
}
