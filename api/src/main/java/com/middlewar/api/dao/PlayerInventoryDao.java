package com.gameserver.dao;

import com.gameserver.model.inventory.PlayerInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface PlayerInventoryDao extends MongoRepository<PlayerInventory, String> {
}
