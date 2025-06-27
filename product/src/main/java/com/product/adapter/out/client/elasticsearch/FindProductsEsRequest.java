package com.product.adapter.out.client.elasticsearch;

import static com.product.domain.model.Category.TOTAL;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.adapter.out.client.elasticsearch.FindProductsEsRequest.Query.Bool;
import com.product.application.port.in.command.FindProductListCommand;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record FindProductsEsRequest(
    Query query,
    List<Map<String, String>> sort,
    Integer from,
    Integer size
) {

    @Builder
    public record Query(
        Bool bool
    ) {

        @Builder
        public record Bool(
            List<Map<String, Map<String, String>>> must,
            List<Map<String, Map<String, String>>> should,
            @JsonProperty("minimum_should_match")
            Integer minimumShouldMatch
        ) {

        }
    }

    static FindProductsEsRequest of(FindProductListCommand command) {
        return FindProductsEsRequest.builder()
            .query(Query.builder()
                .bool(Bool.builder()
                    .must(command.category().equals(TOTAL) ? Collections.emptyList() :
                        List.of(Map.of("term", Map.of("category", command.category().name()))))
                    .should(List.of(
                        Map.of("match", Map.of("keywords", command.query())),
                        Map.of("match_phrase", Map.of("productName", command.query()))))
                    .minimumShouldMatch(1)
                    .build())
                .build())
            .sort(List.of(Map.of(
                command.sortType().type(),
                command.sortType().isDescending() ? "desc" : "asc"
            )))
            .from(command.page())
            .size(command.size())
            .build();
    }
}
