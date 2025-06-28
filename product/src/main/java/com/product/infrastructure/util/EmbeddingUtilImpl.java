package com.product.infrastructure.util;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmbeddingUtilImpl implements EmbeddingUtil {

    private final EmbeddingModel allMiniLmL6V2EmbeddingModel;

    @Override
    public float[] embedToFloatArray(String document) {
        Embedding embedding = allMiniLmL6V2EmbeddingModel.embed(document).content();
        List<Float> values = embedding.vectorAsList();

        float[] vector = new float[values.size()];
        for (int i = 0; i < values.size(); i++) {
            vector[i] = values.get(i);
        }

        return vector;
    }

    @Override
    public float[] averageEmbeddings(List<float[]> embeddings) {
        int dimension = embeddings.getFirst().length;
        float[] sum = new float[dimension];

        for (float[] vec : embeddings) {
            for (int i = 0; i < dimension; i++) {
                sum[i] += vec[i];
            }
        }

        int count = embeddings.size();
        for (int i = 0; i < dimension; i++) {
            sum[i] /= count;
        }

        return sum;
    }
}
