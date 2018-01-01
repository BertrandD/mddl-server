package com.middlewar.core.repository;

import com.middlewar.core.model.instances.ItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface ItemInstanceRepository extends JpaRepository<ItemInstance, Integer> {
}
