package com.order.applicatoin.service.exists_customer_order;

import com.order.applicatoin.port.in.ExistsCustomerOrderUseCase;
import com.order.applicatoin.port.in.command.ExistsCustomerOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ExistsCustomerOrderService implements ExistsCustomerOrderUseCase {

    @Override
    public ExistsCustomerOrderServiceResponse exists(ExistsCustomerOrderCommand command) {
        return null;
    }
}
