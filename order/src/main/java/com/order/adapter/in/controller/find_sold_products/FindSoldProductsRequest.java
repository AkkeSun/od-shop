package com.order.adapter.in.controller.find_sold_products;

import lombok.Builder;

@Builder
record FindSoldProductsRequest(
    Integer page,
    Integer size,
    String searchType,
    Long query
) {

}
