package com.product.adapter.out.persistence.jpa.shard1;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductShard1Repository extends JpaRepository<ProductShard1Entity, Long> {

    Optional<ProductShard1Entity> findByIdAndDeleteYn(Long id, String deleteYn);

    @Modifying
    @Query("delete from ProductShard1Entity p where p.id = :id")
    void deleteById(Long id);

    @Modifying
    @Query("update ProductShard1Entity p set p.deleteYn = 'Y', p.updateDateTime=:deletedAt where p.id = :id")
    void softDeleteById(Long id, LocalDateTime deletedAt);
}
