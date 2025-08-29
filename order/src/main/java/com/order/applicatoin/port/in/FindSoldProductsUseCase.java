package com.order.applicatoin.port.in;

import com.order.applicatoin.port.in.command.FindSoldProductsCommand;
import com.order.applicatoin.service.find_sold_products.FindSoldProductsServiceResponse;

public interface FindSoldProductsUseCase {

    FindSoldProductsServiceResponse findAll(FindSoldProductsCommand command);
}
