package com.order.domain.model;

import java.time.LocalDateTime;

public record Payment(
    Long id,
    int totalPrice,
    LocalDateTime regDateTimes
) {

}
