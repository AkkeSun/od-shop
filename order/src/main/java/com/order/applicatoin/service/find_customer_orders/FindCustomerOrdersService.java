package com.order.applicatoin.service.find_customer_orders;

import com.order.applicatoin.port.in.FindCustomerOrdersUseCase;
import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.applicatoin.port.out.ProductClientPort;
import com.order.applicatoin.service.find_customer_orders.FindCustomerOrdersServiceResponse.FindCustomerOrdersServiceResponseItem;
import com.order.domain.model.Order;
import com.order.domain.model.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindCustomerOrdersService implements FindCustomerOrdersUseCase {

    private final OrderStoragePort orderStoragePort;
    private final ProductClientPort productClientPort;

    @Override
    public FindCustomerOrdersServiceResponse findAll(FindCustomerOrdersCommand command) {
        List<FindCustomerOrdersServiceResponseItem> orderList = new ArrayList<>();

        Page<Order> page = orderStoragePort.findByCustomerId(command);
        for (Order order : page.getContent()) {
            Product product = productClientPort.findProduct(
                order.products().getFirst().getProductId());
            orderList.add(FindCustomerOrdersServiceResponseItem.of(order, product));
        }

        return FindCustomerOrdersServiceResponse.builder()
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .orderList(orderList)
            .build();
    }
}
