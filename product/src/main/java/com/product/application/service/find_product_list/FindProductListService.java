package com.product.application.service.find_product_list;

import com.product.application.port.in.FindProductListUseCase;
import com.product.application.port.in.command.FindProductListCommand;
import com.product.application.port.out.ElasticSearchClientPort;
import com.product.application.port.out.MessageProducerPort;
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
    private final ElasticSearchClientPort elasticSearchClientPort;

    @NewSpan
    @Override
    public FindProductListServiceResponse findProductList(FindProductListCommand command) {
        messageProducerPort.sendMessage(topicName, command.query());
        List<Product> products = elasticSearchClientPort.findProducts(command);

        return FindProductListServiceResponse.of(products, command);
    }
}
