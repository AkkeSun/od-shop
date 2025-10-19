package com.product.adapter.in.controller.increase_product_quantity;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.common.infrastructure.validation.groups.ValidationSequence;
import com.product.application.port.in.IncreaseProductQuantityUseCase;
import com.product.application.service.Increase_product_quantity.IncreaseProductQuantityServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class IncreaseProductQuantityController {

    private final IncreaseProductQuantityUseCase useCase;

    @PutMapping("/products/{productId}/restock")
    ApiResponse<IncreaseProductQuantityResponse> update(
        @Validated(ValidationSequence.class) @RequestBody IncreaseProductQuantityRequest request,
        @LoginAccount LoginAccountInfo account, @PathVariable Long productId
    ) {
        IncreaseProductQuantityServiceResponse serviceResponse = useCase
            .update(productId, request.of(account));

        return ApiResponse.ok(IncreaseProductQuantityResponse.of(serviceResponse));
    }
}
