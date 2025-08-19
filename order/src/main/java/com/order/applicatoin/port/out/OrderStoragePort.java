package com.order.applicatoin.port.out;

import com.order.domain.model.Order;

public interface OrderStoragePort {

    Order register(Order order);
}
