package com.product.adapter.in.controller.register_product;

import com.product.application.port.in.RegisterProductUseCase;
import com.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.domain.model.Account;
import com.product.infrastructure.resolver.LoginAccount;
import com.product.infrastructure.response.ApiResponse;
import com.product.infrastructure.validation.groups.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterProductController {

    private final RegisterProductUseCase useCase;

    @PostMapping("/products")
    ApiResponse<RegisterProductResponse> registerProduct(
        @Validated(ValidationSequence.class) @RequestBody RegisterProductRequest request,
        @LoginAccount Account account
    ) {
        RegisterProductServiceResponse serviceResponse = useCase
            .registerProduct(request.toCommand(account));

        return ApiResponse.ok(RegisterProductResponse.of(serviceResponse));
    }
}
