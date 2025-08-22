package com.order.applicatoin.service.find_customer_orders;

import com.order.applicatoin.port.in.FindCustomerOrdersUseCase;
import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindCustomerOrdersService implements FindCustomerOrdersUseCase {

    @Override
    public FindCustomerOrdersServiceResponse findAll(FindCustomerOrdersCommand command) {
        return null;
    }
}
