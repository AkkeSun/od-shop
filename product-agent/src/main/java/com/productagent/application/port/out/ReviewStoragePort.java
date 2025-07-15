package com.productagent.application.port.out;

import java.util.List;

public interface ReviewStoragePort {

    void deleteByProductIds(List<Long> productIds);

    void deleteByCustomerId(Long customerId);
}
