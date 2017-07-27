package com.middlewar.api.services;

import com.middlewar.api.dao.PlayerInventoryDao;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.PlayerInventory;

/**
 * @author Leboc Philippe.
 */
public interface PlayerInventoryService extends DefaultService<PlayerInventory, PlayerInventoryDao> {
    PlayerInventory create(Player player);
}
