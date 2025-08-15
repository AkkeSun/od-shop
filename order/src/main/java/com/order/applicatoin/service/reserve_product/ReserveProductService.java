package com.order.applicatoin.service.reserve_product;

import com.order.applicatoin.port.in.ReserveProductUseCase;
import com.order.applicatoin.port.in.command.ReserveProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReserveProductService implements ReserveProductUseCase {

    @Override
    public ReserveProductServiceResponse reservation(ReserveProductCommand command) {
        return null;
    }
}
