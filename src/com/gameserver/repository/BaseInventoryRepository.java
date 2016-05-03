package com.gameserver.repository;

import com.gameserver.model.inventory.BaseInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface BaseInventoryRepository extends MongoRepository<BaseInventory, String> {
    BaseInventory findByBase(String id);
}
