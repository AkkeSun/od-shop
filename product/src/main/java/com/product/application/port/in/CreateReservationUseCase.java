package com.product.application.port.in;

import com.product.application.port.in.command.CreateReservationCommand;
import com.product.application.service.create_reservation.CreateReservationServiceResponse;

public interface CreateReservationUseCase {

    CreateReservationServiceResponse reserve(CreateReservationCommand command);
}
