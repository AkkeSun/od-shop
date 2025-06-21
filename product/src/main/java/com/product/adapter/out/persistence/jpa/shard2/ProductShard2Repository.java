package com.product.adapter.out.persistence.jpa.shard2;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductShard2Repository extends JpaRepository<ProductShard2Entity, Long> {

    Optional<ProductShard2Entity> findByIdAndDeleteYn(Long id, String deleteYn);
    
    void deleteById(Long id);

    @Modifying
    @Query("update ProductShard2Entity p set p.deleteYn = 'Y', p.updateDateTime=:deletedAt where p.id = :id")
    void softDeleteById(Long id, LocalDateTime deletedAt);
}
