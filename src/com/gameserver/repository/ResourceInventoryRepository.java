package com.gameserver.repository;

import com.gameserver.model.inventory.ResourceInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface ResourceInventoryRepository extends MongoRepository<ResourceInventory, String> {
    ResourceInventory findByBase(String id);
}