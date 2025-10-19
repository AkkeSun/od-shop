package com.product.application.service.register_review;

import static com.common.infrastructure.exception.ErrorCode.ACCESS_DENIED;
import static com.common.infrastructure.exception.ErrorCode.Business_SAVED_REVIEW_INFO;

import com.common.infrastructure.exception.CustomAuthorizationException;
import com.common.infrastructure.exception.CustomBusinessException;
import com.common.infrastructure.util.SnowflakeGenerator;
import com.product.application.port.in.RegisterReviewUseCase;
import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.application.port.out.OrderClientPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.application.port.out.ReviewStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterReviewService implements RegisterReviewUseCase {

    private final SnowflakeGenerator snowflakeGenerator;
    private final OrderClientPort orderClientPort;
    private final ProductStoragePort productStoragePort;
    private final ReviewStoragePort reviewStoragePort;

    @Override
    public RegisterReviewServiceResponse registerReview(RegisterReviewCommand command) {
        Product product = productStoragePort.findByIdAndDeleteYn(command.productId(), "N");
        if (!orderClientPort.isOrderValid(product.getId(), command.loginInfo().getId())) {
            throw new CustomAuthorizationException(ACCESS_DENIED);
        }
        if (reviewStoragePort.existsByCustomerIdAndProductId(command)) {
            throw new CustomBusinessException(Business_SAVED_REVIEW_INFO);
        }
        reviewStoragePort.register(Review.of(command, snowflakeGenerator.nextId()));
        return RegisterReviewServiceResponse.ofSuccess();
    }
}
