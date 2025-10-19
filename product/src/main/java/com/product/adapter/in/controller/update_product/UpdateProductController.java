package com.product.adapter.in.controller.update_product;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.common.infrastructure.validation.groups.ValidationSequence;
import com.product.application.port.in.UpdateProductUseCase;
import com.product.application.service.update_product.UpdateProductServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UpdateProductController {

    private final UpdateProductUseCase useCase;

    @PutMapping("/products/{productId}")
    ApiResponse<UpdateProductResponse> updateProduct(
        @PathVariable Long productId,
        @RequestBody @Validated(ValidationSequence.class) UpdateProductRequest request,
        @LoginAccount LoginAccountInfo loginInfo
    ) {
        UpdateProductServiceResponse serviceResponse = useCase
            .updateProduct(request.toCommand(loginInfo, productId));

        return ApiResponse.ok(UpdateProductResponse.of(serviceResponse));
    }
}
