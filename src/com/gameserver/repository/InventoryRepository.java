package com.gameserver.repository;

import com.gameserver.model.inventory.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface InventoryRepository extends MongoRepository<Inventory, String> {
}
