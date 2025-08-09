package com.product.application.port.in;

import com.product.application.port.in.command.CancelReservationCommand;
import com.product.application.service.cancel_reservation.CancelReservationServiceResponse;

public interface CancelReservationUseCase {
    CancelReservationServiceResponse cancel(CancelReservationCommand command);
}
