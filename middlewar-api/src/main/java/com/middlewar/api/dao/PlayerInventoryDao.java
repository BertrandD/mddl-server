package com.middlewar.api.dao;

import com.middlewar.core.model.inventory.PlayerInventory;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Leboc Philippe.
 */
public interface PlayerInventoryDao extends JpaRepository<PlayerInventory, Long> {
}
