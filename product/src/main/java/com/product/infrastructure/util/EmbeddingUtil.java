package com.product.infrastructure.util;

import java.util.List;

public interface EmbeddingUtil {

    float[] embedToFloatArray(String document);

    float[] averageEmbeddings(List<float[]> embeddings);
}
