package com.order.applicatoin.service.find_sold_products;

import com.order.applicatoin.port.in.FindSoldProductsUseCase;
import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.applicatoin.port.out.ProductClientPort;
import com.order.applicatoin.service.find_sold_products.FindSoldProductsServiceResponse.FindSoldProductsServiceResponseItem;
import com.order.domain.model.OrderProduct;
import com.order.domain.model.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindSoldProductsService implements FindSoldProductsUseCase {

    private final OrderStoragePort orderStoragePort;
    private final ProductClientPort productClientPort;

    @Override
    public FindSoldProductsServiceResponse findAll(FindSoldProductsCommand command) {
        List<FindSoldProductsServiceResponseItem> orderList = new ArrayList<>();
        Page<OrderProduct> page = orderStoragePort.findSoldProducts(command);
        for (OrderProduct orderProduct : page) {
            Product product = productClientPort.findProduct(orderProduct.getProductId());
            orderList.add(FindSoldProductsServiceResponseItem.of(
                product, orderProduct, orderProduct.getCustomerId()));
        }

        return FindSoldProductsServiceResponse.of(page, orderList);
    }
}
