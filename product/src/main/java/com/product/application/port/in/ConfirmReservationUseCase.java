package com.product.application.port.in;

import com.product.application.port.in.command.ConfirmReservationCommand;
import com.product.application.service.confirm_reeservation.ConfirmReservationServiceResponse;

public interface ConfirmReservationUseCase {

    ConfirmReservationServiceResponse confirmReservation(ConfirmReservationCommand command);
}
