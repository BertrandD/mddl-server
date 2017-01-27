package com.gameserver.dao;

import com.gameserver.model.inventory.ItemContainer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface ItemContainerDao extends MongoRepository<ItemContainer, String> {
}
