package com.order.adapter.in.controller.find_customer_orders;

import com.order.application.port.in.FindCustomerOrdersUseCase;
import com.order.application.service.find_customer_orders.FindCustomerOrdersServiceResponse;
import com.order.domain.model.Account;
import com.order.infrastructure.resolver.LoginAccount;
import com.order.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindCustomerOrdersController {

    private final FindCustomerOrdersUseCase useCase;

    @GetMapping("/orders/buy-products")
    ApiResponse<FindCustomerOrdersResponse> findAll(
        @LoginAccount Account account,
        FindCustomerOrdersRequest request
    ) {
        FindCustomerOrdersServiceResponse serviceResponse = useCase.findAll(
            request.toCommand(account));

        return ApiResponse.ok(FindCustomerOrdersResponse.of(serviceResponse));
    }
}
