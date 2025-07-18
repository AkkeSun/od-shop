package com.productagent.application.port.out;

import com.productagent.domain.model.Order;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderClientPort {

    List<Order> findByOrderDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
