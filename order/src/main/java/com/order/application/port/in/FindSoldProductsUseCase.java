package com.order.application.port.in;

import com.order.application.port.in.command.FindSoldProductsCommand;
import com.order.application.service.find_sold_products.FindSoldProductsServiceResponse;

public interface FindSoldProductsUseCase {

    FindSoldProductsServiceResponse findAll(FindSoldProductsCommand command);
}
