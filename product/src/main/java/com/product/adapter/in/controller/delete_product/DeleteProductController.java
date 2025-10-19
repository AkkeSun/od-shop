package com.product.adapter.in.controller.delete_product;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.product.application.port.in.DeleteProductUseCase;
import com.product.application.service.delete_product.DeleteProductServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteProductController {

    private final DeleteProductUseCase useCase;

    @DeleteMapping("/products/{productId}")
    ApiResponse<DeleteProductResponse> deleteProduct(
        @PathVariable Long productId,
        @LoginAccount LoginAccountInfo loginInfo
    ) {
        DeleteProductServiceResponse serviceResponse = useCase.deleteProduct(productId, loginInfo);

        return ApiResponse.ok(DeleteProductResponse.of(serviceResponse));
    }
}
