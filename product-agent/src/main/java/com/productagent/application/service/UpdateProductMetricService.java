package com.productagent.application.service;

import static com.productagent.infrastructure.constant.CollectionName.METRIC_UPDATE_TIME;

import com.common.infrastructure.util.DateUtil;
import com.productagent.application.port.in.UpdateProductMetricUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.application.port.out.OrderClientPort;
import com.productagent.application.port.out.ProductStoragePort;
import com.productagent.application.port.out.ReviewStoragePort;
import com.productagent.domain.model.MetricUpdateTime;
import com.productagent.domain.model.Order;
import com.productagent.domain.model.Product;
import com.productagent.domain.model.ProductClickLog;
import com.productagent.domain.model.Review;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UpdateProductMetricService implements UpdateProductMetricUseCase {

    private final LogStoragePort logStoragePort;
    private final ReviewStoragePort reviewStoragePort;
    private final OrderClientPort orderClientPort;
    private final ProductStoragePort productStoragePort;

    @Override
    public void update() {
        boolean isLastTime = false;
        LocalDateTime startTime = logStoragePort.findLastMetricUpdateTime().plusSeconds(1);
        LocalDateTime lastTime = DateUtil.getCurrentLocalDateTime();

        if (!startTime.toLocalDate().equals(lastTime.toLocalDate())) {
            isLastTime = true;
            lastTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MAX);
        }

        Map<Long, Product> productMap = new HashMap<>();
        for (Review review : reviewStoragePort.findByRegDateTime(startTime, lastTime)) {
            Product product = productMap.computeIfAbsent(review.productId(), id ->
                productStoragePort.findByIdAndDeleteYn(id, "N")
            );
            if (product != null) {
                product.updateReviewInfo(review);
            }
        }

        for (Order order : orderClientPort.findByOrderDateTime(startTime, lastTime)) {
            Product product = productMap.computeIfAbsent(order.productId(), id ->
                productStoragePort.findByIdAndDeleteYn(id, "N")
            );
            if (product != null) {
                product.updateSalesCount();
            }
        }

        for (ProductClickLog clickLog : logStoragePort.findClickLogBetween(startTime, lastTime)) {
            Product product = productMap.computeIfAbsent(clickLog.productId(), id ->
                productStoragePort.findByIdAndDeleteYn(id, "N")
            );
            if (product != null) {
                product.updateHitCount();
            }
        }

        productStoragePort.registerMetrics(productMap.values().stream()
            .map(product -> {
                product.updateTotalScore();
                return product;
            })
            .toList());

        MetricUpdateTime metricUpdateTime = isLastTime ?
            MetricUpdateTime.of(DateUtil.formatDateTime(lastTime.plusSeconds(1))) :
            MetricUpdateTime.of(DateUtil.formatDateTime(lastTime));

        logStoragePort.register(metricUpdateTime, METRIC_UPDATE_TIME);
        log.info("[updateMetric] success - {}", productMap.size());
    }
}
