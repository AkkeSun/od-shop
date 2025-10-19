package com.product.application.service.create_reservation;

import static com.common.infrastructure.exception.ErrorCode.Business_OUT_OF_STOCK;

import com.common.infrastructure.exception.CustomBusinessException;
import com.common.infrastructure.util.ShardKeyUtil;
import com.common.infrastructure.util.SnowflakeGenerator;
import com.product.application.port.in.CreateReservationUseCase;
import com.product.application.port.in.command.CreateReservationCommand;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import com.product.infrastructure.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateReservationService implements CreateReservationUseCase {

    private final SnowflakeGenerator snowflakeGenerator;
    private final ProductStoragePort productStoragePort;

    @Override
    @DistributedLock(key = "PRODUCT_QUANTITY", isUniqueKey = true)
    public CreateReservationServiceResponse reserve(CreateReservationCommand command) {
        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");
        if (!product.isReservable()) {
            throw new CustomBusinessException(Business_OUT_OF_STOCK);
        }

        product.reserve(command.productQuantity());
        ProductReserveHistory history = ProductReserveHistory.of(getReserveId(command), command);

        ProductReserveHistory reservation = productStoragePort.createReservation(product, history);
        return CreateReservationServiceResponse.of(reservation);
    }

    private long getReserveId(CreateReservationCommand command) {
        if (ShardKeyUtil.isShard1(command.productId())) {
            return snowflakeGenerator.nextId(true);
        }
        return snowflakeGenerator.nextId(false);
    }
}
