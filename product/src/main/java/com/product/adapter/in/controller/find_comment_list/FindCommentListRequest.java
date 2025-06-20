package com.product.adapter.in.controller.find_comment_list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.command.FindCommentListCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindCommentListRequest {

    private int page;
    private int size;

    @Builder
    FindCommentListRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    FindCommentListCommand toCommand(Long productId) {
        return FindCommentListCommand.builder()
            .productId(productId)
            .page(page)
            .size(size == 0 ? 10 : size)
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
