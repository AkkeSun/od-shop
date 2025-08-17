package com.order.applicatoin.port.in;

import com.order.applicatoin.port.in.command.ReserveProductCommand;
import com.order.applicatoin.service.reserve_product.ReserveProductServiceResponse;
import java.util.List;

public interface ReserveProductUseCase {

    List<ReserveProductServiceResponse> reservation(ReserveProductCommand command);
}
