package com.middlewar.api.dao;

import com.middlewar.core.model.inventory.PlayerInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface PlayerInventoryDao extends MongoRepository<PlayerInventory, String> {
}
