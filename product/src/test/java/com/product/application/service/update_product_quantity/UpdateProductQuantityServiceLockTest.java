package com.product.application.service.update_product_quantity;

import com.product.IntegrationTestSupport;
import com.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.application.port.out.MessageProducerPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Account;
import com.product.domain.model.Category;
import com.product.domain.model.Product;
import com.product.domain.model.QuantityType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

class UpdateProductQuantityServiceLockTest extends IntegrationTestSupport {

    @Autowired
    UpdateProductQuantityService service;
    @Autowired
    ProductStoragePort productStoragePort;
    @MockBean
    MessageProducerPort messageProducerPort;

    @Nested
    @DisplayName("[updateProductQuantity] 상품 수량을 업데이트하는 메소드")
    class Describe_updateProductQuantity {

        @Test
        @DisplayName("[success] 다수의 사용자가 동시에 상품수량을 수정하려고 할 때 분산락이 잘 동작하는지 확인한다.")
        void success() throws InterruptedException {
            // given
            Product product = Product.builder()
                .id(snowflakeGenerator.nextId())
                .sellerId(1L)
                .sellerEmail("test@gmail.com")
                .productName("Test Product")
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .keywords(Set.of("keyword1", "keyword2"))
                .price(10000L)
                .quantity(100)
                .category(Category.ELECTRONICS)
                .regDate(LocalDate.of(2025, 5, 1))
                .regDateTime(LocalDateTime.of(2025, 5, 1, 12, 0, 0))
                .salesCount(0L)
                .reviewCount(0L)
                .hitCount(0L)
                .reviewScore(0.0)
                .totalScore(0.0)
                .deleteYn("N")
                .needsEsUpdate(false)
                .build();
            productStoragePort.register(product);
            UpdateProductQuantityCommand command = UpdateProductQuantityCommand.builder()
                .productId(product.getId())
                .account(Account.builder().id(1L).build())
                .quantity(1)
                .type(QuantityType.PURCHASE)
                .build();

            int count = 50;
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(count);
            executor.setMaxPoolSize(count);
            executor.initialize();
            CountDownLatch latch = new CountDownLatch(count);
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            // when
            for (int i = 0; i < count; i++) {
                executor.execute(() -> {
                    try {
                        RequestContextHolder.setRequestAttributes(requestAttributes);
                        service.updateProductQuantity(command);
                    } catch (Exception e) {
                        e.getStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            Product updatedProduct = productStoragePort.findByIdAndDeleteYn(product.getId(), "N");

            // then
            assert updatedProduct.getQuantity() == 50;
        }
    }
}
