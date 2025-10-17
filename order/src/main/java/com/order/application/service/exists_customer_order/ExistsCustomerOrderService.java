package com.order.application.service.exists_customer_order;

import com.order.application.port.in.ExistsCustomerOrderUseCase;
import com.order.application.port.in.command.ExistsCustomerOrderCommand;
import com.order.application.port.out.OrderStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ExistsCustomerOrderService implements ExistsCustomerOrderUseCase {

    private final OrderStoragePort orderStoragePort;
    
    @Override
    public ExistsCustomerOrderServiceResponse exists(ExistsCustomerOrderCommand command) {
        boolean result = orderStoragePort.existsCustomerIdAndProductId(command);
        return ExistsCustomerOrderServiceResponse.of(result);
    }
}
