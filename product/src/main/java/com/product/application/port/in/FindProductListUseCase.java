package com.product.application.port.in;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.application.service.find_product_list.FindProductListServiceResponse;
import java.util.List;

public interface FindProductListUseCase {

    List<FindProductListServiceResponse> findProductList(FindProductListCommand command);
}
