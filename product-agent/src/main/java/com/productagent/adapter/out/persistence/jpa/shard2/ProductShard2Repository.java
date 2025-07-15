package com.productagent.adapter.out.persistence.jpa.shard2;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductShard2Repository extends JpaRepository<ProductShard2Entity, Long> {

    Optional<ProductShard2Entity> findByIdAndDeleteYn(Long id, String deleteYn);
}
