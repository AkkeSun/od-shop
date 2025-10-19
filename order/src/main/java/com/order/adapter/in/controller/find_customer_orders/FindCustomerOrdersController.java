package com.order.adapter.in.controller.find_customer_orders;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.order.application.port.in.FindCustomerOrdersUseCase;
import com.order.application.service.find_customer_orders.FindCustomerOrdersServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindCustomerOrdersController {

    private final FindCustomerOrdersUseCase useCase;

    @GetMapping("/orders/buy-products")
    ApiResponse<FindCustomerOrdersResponse> findAll(
        @LoginAccount LoginAccountInfo loginInfo,
        FindCustomerOrdersRequest request
    ) {
        FindCustomerOrdersServiceResponse serviceResponse = useCase.findAll(
            request.toCommand(loginInfo));

        return ApiResponse.ok(FindCustomerOrdersResponse.of(serviceResponse));
    }
}
