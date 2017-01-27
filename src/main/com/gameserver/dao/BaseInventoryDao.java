package com.gameserver.dao;

import com.gameserver.model.inventory.BaseInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface BaseInventoryDao extends MongoRepository<BaseInventory, String> {
}
