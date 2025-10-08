package com.product.adapter.in.controller.find_review_list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.command.FindReviewListCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FindReviewListRequest {

    private int page;
    private int size;

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
