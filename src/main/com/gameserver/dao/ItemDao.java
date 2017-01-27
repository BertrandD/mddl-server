package com.gameserver.dao;

import com.gameserver.model.instances.ItemInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface ItemDao extends MongoRepository<ItemInstance, String> {
}
