package com.productagent.adapter.out.persistence.jpa.shard2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductShard2Repository extends JpaRepository<ProductShard2Entity, Long> {

    Optional<ProductShard2Entity> findByIdAndDeleteYn(Long id, String deleteYn);

    @Query("SELECT p.id FROM ProductShard2Entity p WHERE p.sellerId = :sellerId")
    List<Long> findIdBySellerId(Long sellerId);

    @Modifying
    @Query("update ProductShard2Entity p set p.deleteYn = 'Y', p.updateDateTime=:deletedAt where p.sellerId = :sellerId")
    void softDeleteByIdSellerId(Long sellerId, LocalDateTime deletedAt);
}
