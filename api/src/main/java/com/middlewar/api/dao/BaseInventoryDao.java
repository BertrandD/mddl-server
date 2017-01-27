package com.middlewar.api.dao;

import com.middlewar.core.model.inventory.BaseInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface BaseInventoryDao extends MongoRepository<BaseInventory, String> {
}
