package com.order.application.port.in;

import com.order.application.port.in.command.FindCustomerOrdersCommand;
import com.order.application.service.find_customer_orders.FindCustomerOrdersServiceResponse;

public interface FindCustomerOrdersUseCase {

    FindCustomerOrdersServiceResponse findAll(FindCustomerOrdersCommand command);
}
