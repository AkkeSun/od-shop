package com.order.applicatoin.port.in;

import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.applicatoin.service.find_customer_orders.FindCustomerOrdersServiceResponse;

public interface FindCustomerOrdersUseCase {

    FindCustomerOrdersServiceResponse findAll(FindCustomerOrdersCommand command);
}
