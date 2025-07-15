package com.productagent.adapter.out.persistence.jpa.shard1;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductShard1Repository extends JpaRepository<ProductShard1Entity, Long> {

    Optional<ProductShard1Entity> findByIdAndDeleteYn(Long id, String deleteYn);

}
