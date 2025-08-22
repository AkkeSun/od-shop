package com.order.adapter.in.controller.find_customer_orders;

import lombok.Builder;

@Builder
record FindCustomerOrdersRequest(
    Integer page,
    Integer size
) {

}
