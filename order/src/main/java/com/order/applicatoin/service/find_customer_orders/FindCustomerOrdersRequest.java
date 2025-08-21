package com.order.applicatoin.service.find_customer_orders;

import lombok.Builder;

@Builder
record FindCustomerOrdersRequest(
    Integer page,
    Integer size
) {

}
