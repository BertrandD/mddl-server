package com.gameserver.services;

import com.gameserver.model.Player;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.repository.PlayerInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class PlayerInventoryService{

    @Autowired
    private PlayerInventoryRepository repository;

    @Autowired
    private PlayerService playerService;

    public PlayerInventory findOne(String id){
        return repository.findOne(id);
    }

    public PlayerInventory findByPlayer(String id) { return repository.findByPlayer(id); }

    public PlayerInventory create(Player player){
        PlayerInventory inventory = new PlayerInventory(player);
        inventory = repository.save(inventory);
        player.setInventory(inventory);
        playerService.update(player);
        return inventory;
    }

    @Async
    public void update(PlayerInventory inventory) { repository.save(inventory); }

    @Async
    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }


}
