package com.product.adapter.in.controller.update_product_quantity;

import com.product.application.port.in.UpdateProductQuantityUseCase;
import com.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
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
class UpdateProductQuantityController {

    private final UpdateProductQuantityUseCase updateProductQuantity;

    @PutMapping("/products/{productId}/quantity")
    ApiResponse<UpdateProductQuantityResponse> updateProductQuantity(
        @Validated(ValidationSequence.class) @RequestBody UpdateProductQuantityRequest request,
        @LoginAccount Account account, @PathVariable Long productId
    ) {

        UpdateProductQuantityServiceResponse serviceResponse = updateProductQuantity
            .updateProductQuantity(request.of(productId, account));
        return ApiResponse.ok(null);
    }
}
