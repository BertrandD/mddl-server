package com.middlewar.api.dao;

import com.middlewar.core.model.instances.ItemInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface ItemDao extends MongoRepository<ItemInstance, String> {
}
