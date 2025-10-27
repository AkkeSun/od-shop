package com.productagent.fakeClass;

import com.productagent.application.port.out.OrderClientPort;
import com.productagent.domain.model.Order;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeOrderClientPort implements OrderClientPort {

    public List<Order> orders = new ArrayList<>();
    public boolean shouldThrowException = false;

    @Override
    public List<Order> findByOrderDateTime(LocalDateTime startDateTime,
        LocalDateTime endDateTime) {
        if (shouldThrowException) {
            throw new RuntimeException("Simulated exception");
        }

        log.info("FakeOrderClientPort find orders between {} and {}", startDateTime, endDateTime);
        return orders;
    }
}
