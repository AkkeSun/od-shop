package com.order.applicatoin.service.find_order_product_ids;

import com.order.applicatoin.port.in.FindOrderProductIdsUseCase;
import com.order.applicatoin.port.in.command.FindOrderProductIdsCommand;
import com.order.applicatoin.port.out.OrderStoragePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindOrderProductIdsService implements FindOrderProductIdsUseCase {

    private final OrderStoragePort orderStoragePort;

    @Override
    public List<Long> findOrderProductIds(FindOrderProductIdsCommand command) {
        return orderStoragePort.findOrderProductIds(command);
    }
}
