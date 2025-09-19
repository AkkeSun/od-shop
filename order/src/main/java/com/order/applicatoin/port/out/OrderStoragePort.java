package com.order.applicatoin.port.out;

import com.order.applicatoin.port.in.command.ExistsCustomerOrderCommand;
import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.applicatoin.port.in.command.FindOrderProductIdsCommand;
import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import java.util.List;
import org.springframework.data.domain.Page;

public interface OrderStoragePort {

    Order register(Order order);

    void cancel(Order order);

    Page<Order> findByCustomerId(FindCustomerOrdersCommand command);
    
    Page<OrderProduct> findSoldProducts(FindSoldProductsCommand command);

    Order findById(Long id);

    boolean existsCustomerIdAndProductId(ExistsCustomerOrderCommand command);

    List<Long> findOrderProductIds(FindOrderProductIdsCommand command);
}
