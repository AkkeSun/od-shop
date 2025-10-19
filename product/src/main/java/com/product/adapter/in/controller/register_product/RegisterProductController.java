package com.product.adapter.in.controller.register_product;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.common.infrastructure.validation.groups.ValidationSequence;
import com.product.application.port.in.RegisterProductUseCase;
import com.product.application.service.register_product.RegisterProductServiceResponse;
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
        @LoginAccount LoginAccountInfo loginInfo
    ) {
        RegisterProductServiceResponse serviceResponse = useCase
            .registerProduct(request.toCommand(loginInfo));

        return ApiResponse.ok(RegisterProductResponse.of(serviceResponse));
    }
}
