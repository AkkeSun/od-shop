package com.order.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
    Long orderNumber,
    Long customerId,
    String buyStatus,
    List<Product> productList,
    Payment payment,
    LocalDateTime regDateTime
) {

}
