package com.product.application.port.in;

import com.product.application.port.in.command.FindCommentListCommand;
import com.product.application.service.find_comment_list.FindCommentListServiceResponse;
import java.util.List;

public interface FindCommentListUseCase {

    List<FindCommentListServiceResponse> findCommentList(FindCommentListCommand command);
}
