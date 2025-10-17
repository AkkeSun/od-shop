package com.order.application.port.in;

import com.order.application.port.in.command.FindOrderProductIdsCommand;
import java.util.List;

public interface FindOrderProductIdsUseCase {

    List<Long> findOrderProductIds(FindOrderProductIdsCommand command);
}
