package com.productagent.application.service;

import com.productagent.application.port.in.DeleteProductUseCase;
import com.productagent.application.port.out.ElasticSearchClientPort;
import com.productagent.application.port.out.ProductStoragePort;
import com.productagent.application.port.out.ReviewStoragePort;
import com.productagent.domain.model.DeleteAccountLog;
import com.productagent.infrastructure.util.JsonUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteProductService implements DeleteProductUseCase {

    private final ReviewStoragePort reviewStoragePort;
    private final ProductStoragePort productStoragePort;
    private final ElasticSearchClientPort elasticSearchClientPort;

    @Override
    public void delete(String payload) {
        DeleteAccountLog account = JsonUtil.parseJson(payload, DeleteAccountLog.class);
        if (ObjectUtils.isEmpty(account)) {
            log.error("[deleteProduct] parsingFailed - {} ", payload);
        }

        if (account.isSeller()) {
            List<Long> productIds = productStoragePort.findIdBySellerId(account.accountId());
            productStoragePort.deleteBySellerId(account.accountId());
            reviewStoragePort.deleteByProductIds(productIds);
            elasticSearchClientPort.deleteByIds(productIds);
        } else {
            reviewStoragePort.deleteByCustomerId(account.accountId());
        }
    }
}
