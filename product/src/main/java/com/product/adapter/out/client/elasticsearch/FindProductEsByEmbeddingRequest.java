package com.product.adapter.out.client.elasticsearch;

import lombok.Builder;

@Builder
record FindProductEsByEmbeddingRequest(
    int size,
    Knn knn

) {

    @Builder
    record Knn(
        String field,
        float[] query_vector,
        int k,
        int num_candidates
    ) {

    }

    static FindProductEsByEmbeddingRequest of(
        float[] embedding
    ) {
        return FindProductEsByEmbeddingRequest.builder()
            .size(30)
            .knn(Knn.builder()
                .field("embedding")
                .query_vector(embedding)
                .k(30)
                .num_candidates(100)
                .build())
            .build();
    }
}
