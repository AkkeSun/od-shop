package com.product.application.service.find_product_list;

import com.product.application.port.in.FindProductListUseCase;
import com.product.application.port.in.command.FindProductListCommand;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductEsStoragePort;
import com.product.domain.model.Product;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindProductListService implements FindProductListUseCase {

    @Value("${kafka.topic.search}")
    private String topicName;
    private final MessageProducerPort messageProducerPort;
    private final ProductEsStoragePort productEsStoragePort;

    @NewSpan
    @Override
    public List<FindProductListServiceResponse> findProductList(FindProductListCommand command) {
        messageProducerPort.sendMessage(topicName, command.query());
        List<Product> products = productEsStoragePort.findByCategoryAndKeywords(command);

        return products.stream()
            .map(FindProductListServiceResponse::of)
            .toList();
    }
}
