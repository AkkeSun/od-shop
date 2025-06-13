package com.product.adapter.out.persistence.elasticSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

interface ProductEsRepository extends ElasticsearchRepository<ProductEsDocument, Long> {

}
