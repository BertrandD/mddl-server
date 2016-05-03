package com.gameserver.repository;

import com.gameserver.enums.ItemType;
import com.gameserver.model.instances.ItemInstance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface ItemRepository extends MongoRepository<ItemInstance, String> {
    List<ItemInstance> findByInventoryIdAndType(String inventoryId, ItemType type);
}
