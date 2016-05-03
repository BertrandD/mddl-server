package com.gameserver.repository;

import com.gameserver.model.inventory.PlayerInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author LEBOC Philippe
 */
public interface PlayerInventoryRepository extends MongoRepository<PlayerInventory, String> {
    PlayerInventory findByPlayer(String id);
}
