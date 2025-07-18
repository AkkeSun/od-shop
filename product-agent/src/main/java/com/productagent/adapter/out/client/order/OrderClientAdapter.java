package com.productagent.adapter.out.client.order;

import com.productagent.application.port.out.OrderClientPort;
import com.productagent.domain.model.Order;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class OrderClientAdapter implements OrderClientPort {

    // TODO: 작업 필요
    @Override
    public List<Order> findByOrderDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return List.of();
    }
}
