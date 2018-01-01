package com.middlewar.core.repository;

import com.middlewar.core.model.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
