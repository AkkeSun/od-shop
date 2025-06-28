package com.product.adapter.out.persistence.jpa;

import com.product.adapter.out.persistence.jpa.shard1.RecommendShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.RecommendShard2Adapter;
import com.product.application.port.out.RecommendStoragePort;
import com.product.domain.model.ProductRecommend;
import com.product.domain.model.RecommendType;
import io.micrometer.tracing.annotation.NewSpan;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class RecommendStorageAdapter implements RecommendStoragePort {

    private final RecommendShard1Adapter shard1Adapter;
    private final RecommendShard2Adapter shard2Adapter;

    @NewSpan
    @Override
    public List<ProductRecommend> findRecommendProductList(LocalDate checkDate,
        RecommendType type) {
        List<ProductRecommend> recommendProductList = new ArrayList<>();
        recommendProductList.addAll(shard1Adapter.findRecommendProductList(checkDate, type));
        recommendProductList.addAll(shard2Adapter.findRecommendProductList(checkDate, type));
        return recommendProductList;
    }
}
