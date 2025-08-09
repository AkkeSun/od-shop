package com.product.adapter.in.controller.increase_product_quantity;

import com.product.application.port.in.IncreaseProductQuantityUseCase;
import com.product.application.service.Increase_product_quantity.IncreaseProductQuantityServiceResponse;
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
class IncreaseProductQuantityController {

    private final IncreaseProductQuantityUseCase useCase;

    @PutMapping("/products/{productId}/restock")
    ApiResponse<IncreaseProductQuantityResponse> update(
        @Validated(ValidationSequence.class) @RequestBody IncreaseProductQuantityRequest request,
        @LoginAccount Account account, @PathVariable Long productId
    ) {
        IncreaseProductQuantityServiceResponse serviceResponse = useCase
            .update(request.of(productId, account));

        return ApiResponse.ok(IncreaseProductQuantityResponse.builder()
            .result(serviceResponse.result())
            .build());
    }
}
