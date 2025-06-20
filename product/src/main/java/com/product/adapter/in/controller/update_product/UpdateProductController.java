package com.product.adapter.in.controller.update_product;

import com.product.application.port.in.UpdateProductUseCase;
import com.product.application.service.update_product.UpdateProductServiceResponse;
import com.product.domain.model.Account;
import com.product.infrastructure.resolver.LoginAccount;
import com.product.infrastructure.response.ApiResponse;
import com.product.infrastructure.validation.groups.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UpdateProductController {

    private final UpdateProductUseCase updateProductUseCase;

    @PutMapping("/products/{productId}")
    ApiResponse<UpdateProductResponse> updateProduct(
        @PathVariable Long productId,
        @RequestBody @Validated(ValidationSequence.class) UpdateProductRequest request,
        @LoginAccount Account account
    ) {
        UpdateProductServiceResponse serviceResponse = updateProductUseCase
            .updateProduct(request.toCommand(account, productId));

        return ApiResponse.ok(UpdateProductResponse.of(serviceResponse));
    }
}
