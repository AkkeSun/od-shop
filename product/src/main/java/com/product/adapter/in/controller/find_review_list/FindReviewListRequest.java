package com.product.adapter.in.controller.find_review_list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.command.FindReviewListCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Getter
@NoArgsConstructor
class FindReviewListRequest {

    private int page;
    private int size;

    @Builder
    FindReviewListRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    FindReviewListCommand toCommand(Long productId) {
        return FindReviewListCommand.builder()
            .productId(productId)
            .pageable(PageRequest.of(page, size == 0 ? 10 : size))
            .build();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
