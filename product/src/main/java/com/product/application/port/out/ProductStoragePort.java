package com.product.application.port.out;

import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import java.time.LocalDateTime;

public interface ProductStoragePort {

    ProductReserveHistory createReservation(Product product, ProductReserveHistory reserveHistory);

    void confirmReservation(Product product, ProductReserveHistory reserveHistory);

    void cancelReservation(Product product, ProductReserveHistory reserveHistory);

    ProductReserveHistory findReservationById(Long reserveId);

    void register(Product product);

    void deleteById(Long productId);

    void softDeleteById(Long productId, LocalDateTime deleteAt);

    Product findByIdAndDeleteYn(Long productId, String deleteYn);
}
