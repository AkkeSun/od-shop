package com.order.application.service.find_customer_orders;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.order.application.port.in.FindCustomerOrdersUseCase;
import com.order.application.port.in.command.FindCustomerOrdersCommand;
import com.order.application.port.out.OrderStoragePort;
import com.order.application.port.out.ProductClientPort;
import com.order.application.port.out.RedisStoragePort;
import com.order.application.service.find_customer_orders.FindCustomerOrdersServiceResponse.FindCustomerOrdersServiceResponseItem;
import com.order.domain.model.Order;
import com.order.domain.model.Product;
import com.order.infrastructure.properties.RedisProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindCustomerOrdersService implements FindCustomerOrdersUseCase {

    private final RedisProperties redisProperties;
    private final RedisStoragePort redisStoragePort;
    private final OrderStoragePort orderStoragePort;
    private final ProductClientPort productClientPort;

    @Override
    public FindCustomerOrdersServiceResponse findAll(FindCustomerOrdersCommand command) {
        String key = String.format(redisProperties.key().customerOrder(), command.customerId(), command.page(), command.size());
        FindCustomerOrdersServiceResponse response = redisStoragePort.findData(key,
            FindCustomerOrdersServiceResponse.class);

        if (response != null) {
            return response;
        }

        List<FindCustomerOrdersServiceResponseItem> orderList = new ArrayList<>();
        Page<Order> page = orderStoragePort.findByCustomerId(command);
        for (Order order : page.getContent()) {
            Product product = productClientPort.findProduct(
                order.products().getFirst().getProductId());
            orderList.add(FindCustomerOrdersServiceResponseItem.of(order, product));
        }

        response = FindCustomerOrdersServiceResponse.of(page, orderList);
        redisStoragePort.register(key, toJsonString(response), redisProperties.ttl().customerOrder());

        return response;
    }

}
