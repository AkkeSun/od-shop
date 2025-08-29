package com.product.application.service.confirm_reeservation;

import com.product.application.port.in.ConfirmReservationUseCase;
import com.product.application.port.in.command.ConfirmReservationCommand;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import com.product.infrastructure.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ConfirmReservationService implements ConfirmReservationUseCase {

    private final ProductStoragePort productStoragePort;

    @Override
    @DistributedLock(key = "PRODUCT_QUANTITY", isUniqueKey = true)
    public ConfirmReservationServiceResponse confirmReservation(ConfirmReservationCommand command) {
        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");
        ProductReserveHistory reservation = productStoragePort.findReservationById(
            command.reserveId());

        product.confirmReservation(reservation);
        productStoragePort.confirmReservation(product, reservation);
        return ConfirmReservationServiceResponse.of(product, reservation);
    }
}