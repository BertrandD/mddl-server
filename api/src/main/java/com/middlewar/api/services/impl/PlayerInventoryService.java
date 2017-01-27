package com.middlewar.api.services.impl;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.PlayerInventory;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerInventoryService extends DatabaseService<PlayerInventory> {

    protected PlayerInventoryService() {
        super(PlayerInventory.class);
    }

    @Override
    public PlayerInventory create(Object... params) {
        final PlayerInventory inventory = new PlayerInventory();
        if(params.length == 1) inventory.setPlayer((Player)params[0]);
        mongoOperations.insert(inventory);
        return inventory;
    }
}
