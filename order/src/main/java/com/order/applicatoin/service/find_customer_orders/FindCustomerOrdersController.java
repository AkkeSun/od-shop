package com.order.applicatoin.service.find_customer_orders;

import com.order.infrastructure.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FindCustomerOrdersController {

    @GetMapping("/customers/{customerId}/orders")
    ApiResponse<FindCustomerOrdersResponse> findAll(@PathVariable Long customerId,
        FindCustomerOrdersRequest request) {

        return ApiResponse.ok(null);
    }
}
