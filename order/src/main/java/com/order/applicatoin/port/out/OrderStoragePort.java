package com.order.applicatoin.port.out;

import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.domain.model.Order;
import org.springframework.data.domain.Page;

public interface OrderStoragePort {

    Order register(Order order);

    Page<Order> findByCustomerId(FindCustomerOrdersCommand command);

    Page<Order> findSoldProducts(FindSoldProductsCommand command);


}
