package com.order.application.port.in;

import com.order.application.port.in.command.ExistsCustomerOrderCommand;
import com.order.application.service.exists_customer_order.ExistsCustomerOrderServiceResponse;

public interface ExistsCustomerOrderUseCase {

    ExistsCustomerOrderServiceResponse exists(ExistsCustomerOrderCommand command);
}
