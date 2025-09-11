package com.order.applicatoin.port.in;

import com.order.applicatoin.port.in.command.ExistsCustomerOrderCommand;
import com.order.applicatoin.service.exists_customer_order.ExistsCustomerOrderServiceResponse;

public interface ExistsCustomerOrderUseCase {

    ExistsCustomerOrderServiceResponse exists(ExistsCustomerOrderCommand command);
}
