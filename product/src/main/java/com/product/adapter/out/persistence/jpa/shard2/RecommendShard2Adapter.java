package com.product.adapter.out.persistence.jpa.shard2;

import com.product.application.port.out.RecommendStoragePort;
import com.product.domain.model.ProductRecommend;
import com.product.domain.model.RecommendType;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "secondaryTransactionManager")
public class RecommendShard2Adapter implements RecommendStoragePort {

    private final RecommendShard2Repository repository;

    @Override
    public List<ProductRecommend> findRecommendProductList(LocalDate checkDate,
        RecommendType type) {
        return repository.findByCheckDateAndType(checkDate, type)
            .stream()
            .map(RecommendShard2Entity::toDomain)
            .toList();
    }
}
