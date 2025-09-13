package com.order.applicatoin.port.in;

import com.order.applicatoin.port.in.command.FindOrderProductIdsCommand;
import java.util.List;

public interface FindOrderProductIdsUseCase {

    List<Long> findOrderProductIds(FindOrderProductIdsCommand command);
}
