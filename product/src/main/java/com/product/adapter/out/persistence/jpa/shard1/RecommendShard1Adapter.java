package com.product.adapter.out.persistence.jpa.shard1;

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
@Transactional(transactionManager = "primaryTransactionManager")
public class RecommendShard1Adapter implements RecommendStoragePort {

    private final RecommendShard1Repository repository;

    @Override
    public List<ProductRecommend> findRecommendProductList(LocalDate checkDate,
        RecommendType type) {
        return repository.findByCheckDateAndType(checkDate, type)
            .stream()
            .map(RecommendShard1Entity::toDomain)
            .toList();
    }
}
