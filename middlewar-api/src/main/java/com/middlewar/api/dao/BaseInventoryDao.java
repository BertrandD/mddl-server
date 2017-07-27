package com.middlewar.api.dao;

import com.middlewar.core.model.inventory.BaseInventory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Leboc Philippe.
 */
public interface BaseInventoryDao extends JpaRepository<BaseInventory, Long> {
}
