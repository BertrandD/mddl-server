package com.gameserver.repository;

import com.gameserver.model.instances.ItemInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface ItemRepository extends MongoRepository<ItemInstance, String> {
}
