package com.middlewar.api.dao;

import com.middlewar.core.model.inventory.ResourceInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface ItemContainerDao extends MongoRepository<ResourceInventory, String> {
}
