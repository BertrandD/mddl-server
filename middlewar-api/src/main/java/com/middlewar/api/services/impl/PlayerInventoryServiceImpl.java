package com.middlewar.api.services.impl;

import com.middlewar.api.dao.PlayerDao;
import com.middlewar.api.dao.PlayerInventoryDao;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.PlayerInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerInventoryServiceImpl extends DefaultServiceImpl<PlayerInventory, PlayerInventoryDao> implements PlayerInventoryService {

    @Autowired
    private PlayerInventoryDao playerInventoryDao;

    @Autowired
    private PlayerDao playerDao;

    @Override
    public PlayerInventory create(Player player) {
        final PlayerInventory inventory = playerInventoryDao.save(new PlayerInventory(player));
        if (inventory != null) player.setInventory(inventory);
        playerDao.save(player);
        return inventory;
    }
}
