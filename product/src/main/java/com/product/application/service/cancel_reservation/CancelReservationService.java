package com.product.application.service.cancel_reservation;

import com.product.application.port.in.CancelReservationUseCase;
import com.product.application.port.in.command.CancelReservationCommand;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import com.product.infrastructure.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CancelReservationService implements CancelReservationUseCase {

    private final ProductStoragePort productStoragePort;

    @Override
    @DistributedLock(key = "PRODUCT_QUANTITY", isUniqueKey = true)
    public CancelReservationServiceResponse cancel(CancelReservationCommand command) {
        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");
        ProductReserveHistory reservation = productStoragePort
            .findReservationById(command.reserveId());

        product.cancelReservation(reservation);
        productStoragePort.cancelReservation(product, reservation);
        return CancelReservationServiceResponse.ofSuccess();
    }
}
