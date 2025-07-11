package com.product.application.port.in;

import com.product.application.port.in.command.FindReviewListCommand;
import com.product.application.service.find_review_list.FindReviewListServiceResponse;

public interface FindReviewListUseCase {

    FindReviewListServiceResponse findReviewList(FindReviewListCommand command);
}
