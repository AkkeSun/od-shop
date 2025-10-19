package com.order.adapter.in.controller.find_sold_products;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.order.application.port.in.FindSoldProductsUseCase;
import com.order.application.service.find_sold_products.FindSoldProductsServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindSoldProductsController {

    private final FindSoldProductsUseCase useCase;

    @GetMapping("/orders/sold-products")
    ApiResponse<FindSoldProductsResponse> findAll(
        @Valid FindSoldProductsRequest request,
        @LoginAccount LoginAccountInfo loginInfo
    ) {
        FindSoldProductsServiceResponse serviceResponse = useCase.findAll(
            request.toCommand(loginInfo));

        return ApiResponse.ok(FindSoldProductsResponse.of(serviceResponse));
    }
}
