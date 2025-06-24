package com.product.adapter.out.persistence.elasticSearch;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

interface ProductEsRepository extends ElasticsearchRepository<ProductEsDocument, Long> {

    @Query("""
        {
            "bool": {
                "must": [
                    { "term": { "category": "?0" } }
                ],
                "should": [
                    { "match": { "keywords": "?1" } },
                    { "match_phrase": {"productName": "?1" } }
                ],
                "minimum_should_match": 1
            }
        }
         """)
    List<ProductEsDocument> findByCategoryAndQuery(String category, String query,
        Pageable pageable);

    @Query("""
        {
            "bool": {
                "should": [
                        { "match": { "keywords": "?0" } },
                        { "match_phrase": {"productName": "?0" } }
                ],
                "minimum_should_match": 1
            }
        }
        """)
    List<ProductEsDocument> findByQuery(String query, Pageable pageable);
}
