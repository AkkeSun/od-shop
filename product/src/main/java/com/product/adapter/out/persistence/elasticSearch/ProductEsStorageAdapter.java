package com.product.adapter.out.persistence.elasticSearch;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.application.port.out.ProductEsStoragePort;
import com.product.domain.model.Product;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductEsStorageAdapter implements ProductEsStoragePort {

    private final ProductEsRepository repository;

    @NewSpan
    @Override
    public void register(Product product, float[] embedding) {
        repository.save(ProductEsDocument.of(product, embedding));
    }

    @NewSpan
    @Override
    public void deleteById(Long productId) {
        repository.deleteById(productId);
    }

    @NewSpan
    @Override
    public List<Product> findByCategoryAndKeywords(FindProductListCommand command) {
        List<ProductEsDocument> documents = command.isTotalCategorySearch() ?
            repository.findByQuery(command.query(), command.pageable()) :
            repository.findByCategoryAndQuery(command.category().name(), command.query(),
                command.pageable());

        return documents.stream()
            .map(ProductEsDocument::toDomain)
            .collect(Collectors.toList());
    }
}
