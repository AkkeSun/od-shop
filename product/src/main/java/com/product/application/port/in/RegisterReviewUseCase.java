package com.product.application.port.in;

import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.application.service.register_review.RegisterReviewServiceResponse;

public interface RegisterReviewUseCase {

    RegisterReviewServiceResponse registerReview(RegisterReviewCommand command);
}
