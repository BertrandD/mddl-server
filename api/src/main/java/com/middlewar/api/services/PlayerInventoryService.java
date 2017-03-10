package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.PlayerInventory;

/**
 * @author Leboc Philippe.
 */
public interface PlayerInventoryService extends DefaultService<PlayerInventory> {
    PlayerInventory create(Player player);
}
