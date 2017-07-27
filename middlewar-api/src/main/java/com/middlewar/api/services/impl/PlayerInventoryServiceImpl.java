package com.middlewar.api.services.impl;

import com.middlewar.api.dao.PlayerInventoryDao;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.PlayerInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerInventoryServiceImpl implements PlayerInventoryService {

    @Autowired
    private PlayerInventoryDao playerInventoryDao;

    @Autowired
    private PlayerService playerService;

    @Override
    public PlayerInventory create(Player player) {
        final PlayerInventory inventory = playerInventoryDao.save(new PlayerInventory(player));
        if (inventory != null) player.setInventory(inventory);
        playerService.update(player);
        return inventory;
    }

    @Override
    public PlayerInventory findOne(long id) {
        return null;
    }

    @Override
    public List<PlayerInventory> findAll() {
        return playerInventoryDao.findAll();
    }

    @Override
    public void update(PlayerInventory object) {

    }

    @Override
    public void remove(PlayerInventory object) {
        playerInventoryDao.delete(object);
    }

    @Override
    public void deleteAll() {
        playerInventoryDao.deleteAll();
    }
}
