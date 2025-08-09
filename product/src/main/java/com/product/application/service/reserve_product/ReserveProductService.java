package com.product.application.service.reserve_product;

import static com.product.infrastructure.exception.ErrorCode.Business_OUT_OF_STOCK;

import com.product.application.port.in.ReserveProductUseCase;
import com.product.application.port.in.command.ReserveProductCommand;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import com.product.infrastructure.aop.DistributedLock;
import com.product.infrastructure.exception.CustomBusinessException;
import com.product.infrastructure.util.ShardKeyUtil;
import com.product.infrastructure.util.SnowflakeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReserveProductService implements ReserveProductUseCase {

    private final SnowflakeGenerator snowflakeGenerator;
    private final ProductStoragePort productStoragePort;

    @Override
    @DistributedLock(key = "PRODUCT_QUANTITY", isUniqueKey = true)
    public ReserveProductServiceResponse reserve(ReserveProductCommand command) {
        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");
        if (!product.isReservable()) {
            throw new CustomBusinessException(Business_OUT_OF_STOCK);
        }

        product.reserve(command.productQuantity());
        ProductReserveHistory history = ProductReserveHistory.of(getReserveId(command), command);

        ProductReserveHistory reservation = productStoragePort.createReservation(product, history);
        return ReserveProductServiceResponse.of(reservation);
    }

    private long getReserveId(ReserveProductCommand command) {
        if (ShardKeyUtil.isShard1(command.productId())) {
            return snowflakeGenerator.nextId(true);
        }
        return snowflakeGenerator.nextId(false);
    }
}
