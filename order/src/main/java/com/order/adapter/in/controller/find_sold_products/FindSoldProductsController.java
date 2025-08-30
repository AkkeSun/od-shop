package com.order.adapter.in.controller.find_sold_products;

import com.order.applicatoin.port.in.FindSoldProductsUseCase;
import com.order.applicatoin.service.find_sold_products.FindSoldProductsServiceResponse;
import com.order.domain.model.Account;
import com.order.infrastructure.resolver.LoginAccount;
import com.order.infrastructure.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindSoldProductsController {

    private final FindSoldProductsUseCase useCase;

    @GetMapping("/orders/sold-products")
    ApiResponse<FindSoldProductsResponse> findAll(@Valid FindSoldProductsRequest request,
        @LoginAccount Account account) {
        FindSoldProductsServiceResponse serviceResponse = useCase.findAll(
            request.toCommand(account));

        return ApiResponse.ok(FindSoldProductsResponse.of(serviceResponse));
    }
}
