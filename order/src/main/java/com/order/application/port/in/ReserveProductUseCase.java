package com.order.application.port.in;

import com.order.application.port.in.command.ReserveProductCommand;
import com.order.application.service.reserve_product.ReserveProductServiceResponse;
import java.util.List;

public interface ReserveProductUseCase {

    List<ReserveProductServiceResponse> reservation(ReserveProductCommand command);
}
